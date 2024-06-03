package it.einjojo.akani.boss.room;

import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.storage.jsonfile.JsonRoomDataFile;
import it.einjojo.akani.boss.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class RoomManager {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RoomManager.class);
    private final JavaPlugin plugin;
    private final BossSystem internal;
    private final Path templatesFolder;
    private final List<ActiveRoom> loadedRooms = new LinkedList<>();
    private final List<RoomTemplate> roomTemplatesRequiringSetup = new LinkedList<>();
    private final Map<String, RoomTemplate> roomTemplates = new ConcurrentHashMap<>();

    public RoomManager(JavaPlugin plugin, BossSystem internal) {
        this.plugin = plugin;
        templatesFolder = plugin.getDataFolder().toPath().resolve("templates");
        this.internal = internal;
    }


    /**
     * Loads room templates from the specified folder. If the folder does not exist, it creates the folder.
     * For each directory in the folder, it loads the room data from the "roomdata.json" file and creates a RoomTemplate object.
     * If the room data cannot be loaded, a placeholder is used.
     * The RoomTemplate objects are added to the roomTemplates map.
     */
    @Blocking
    public void load() {
        plugin.getLogger().info("Loading room templates...");
        if (!Files.exists(templatesFolder)) {
            plugin.getLogger().info("Templates folder does not exist, creating...");
            try {
                plugin.getLogger().info("Creating templates folder...");
                Files.createDirectories(templatesFolder);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create templates folder: " + e.getMessage());
                return;
            }
        }
        try (Stream<Path> stream = Files.walk(templatesFolder, 1)) {
            for (Iterator<Path> it = stream.iterator(); it.hasNext(); ) {
                Path path = it.next();
                if (path.equals(templatesFolder)) continue;
                if (!Files.isDirectory(path)) continue;
                RoomData data = new JsonRoomDataFile(internal.gson(), path.resolve("roomdata.json")).load();
                if (data == null) {
                    data = RoomData.PLACEHOLDER;
                    logger().warning("Failed to load roomdata.json for template " + path.getFileName().toString());
                }
                RoomTemplate template = new RoomTemplate(path.getFileName().toString(), path, data);
                if (data == RoomData.PLACEHOLDER) {
                    roomTemplatesRequiringSetup.add(template);
                }
                template.cleanFolder();
                roomTemplates.put(template.templateName(), template);
                plugin.getLogger().info("Loaded room template: " + template.templateName());
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load room templates: " + e.getMessage());
        }
    }

    protected Logger logger() {
        return plugin.getLogger();
    }


    public CompletableFuture<ActiveRoom> createRoomByTemplate(String templateName) {
        RoomTemplate template = roomTemplates.get(templateName);
        if (template == null) throw new IllegalArgumentException("Room Template not found");
        return createRoomByTemplate(template);
    }

    public CompletableFuture<ActiveRoom> createRoomByTemplate(RoomTemplate template) {
        ActiveRoom room = new ActiveRoom(UUID.randomUUID(), template);
        CompletableFuture<ActiveRoom> future = new CompletableFuture<>();
        room.createWorld(plugin).whenComplete((world, throwable) -> {
            if (throwable != null) {
                plugin.getLogger().severe("Failed to create world for room " + template.templateName() + ": " + throwable.getMessage());
                future.completeExceptionally(throwable);
                return;
            }
            loadedRooms.add(room);
            future.complete(room);
        });
        return future;
    }

    public void updateRoomTemplate(RoomTemplate roomTemplate) {
        if (roomTemplate.roomData().equals(RoomData.PLACEHOLDER)) return;
        CompletableFuture.runAsync(() -> {
            new JsonRoomDataFile(internal.gson(), roomTemplate.roomDataPath()).save(roomTemplate.roomData());
            roomTemplates.put(roomTemplate.templateName(), roomTemplate);
            logger().info("Updated room template: " + roomTemplate.templateName());
        }).exceptionally(throwable -> {
            logger().warning("Failed to save roomdata.json for template " + roomTemplate.templateName() + ": " + throwable.getMessage());
            return null;
        });

    }

    public @Nullable RoomTemplate roomTemplate(String templateName) {
        return roomTemplates.get(templateName);
    }

    public List<ActiveRoom> loadedRooms() {
        return loadedRooms;
    }

    public void deleteActiveRoom(ActiveRoom activeRoom) {
        loadedRooms.remove(activeRoom);
        try {
            activeRoom.deleteWorld();
        } catch (IOException e) {
            logger().warning("Failed to delete world for room " + activeRoom.template().templateName() + ": " + e.getMessage());
        }
    }


    public Map<String, RoomTemplate> roomTemplates() {
        return roomTemplates;
    }

    /**
     * Runs a cleanup task to delete all room worlds that are not used anymore
     *
     * @return A CompletableFuture that completes when the cleanup is done
     */
    public CompletableFuture<Void> cleanupWorldContainer() {
        return CompletableFuture.supplyAsync(() -> {
            List<World> unloadCheck = new LinkedList<>();
            List<Path> toDelete = new LinkedList<>();
            try (Stream<Path> stream = Files.walk(Bukkit.getWorldContainer().toPath())) {
                stream.filter(path -> path.getFileName().toString().startsWith(ActiveRoom.WORLD_NAME_PREFIX)).forEach(worldPath -> {
                    World world = Bukkit.getWorld(worldPath.getFileName().toString());
                    if (world == null) {
                        toDelete.add(worldPath);
                        return;
                    }
                    // Only delete worlds that are not used by any player
                    if (world.getPlayerCount() == 0) {
                        toDelete.add(worldPath);
                        unloadCheck.add(world);
                    }
                });
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to cleanup world container: " + e.getMessage());
                throw new RuntimeException(e);
            }
            if (unloadCheck.isEmpty()) {
                return toDelete;
            }
            // Sync unload worlds
            CompletableFuture<List<Path>> unloadFuture = new CompletableFuture<>();
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                unloadCheck.forEach((world) -> {
                    Bukkit.unloadWorld(world, false);
                });
                unloadFuture.complete(toDelete);
            });
            return unloadFuture.join();
        }).exceptionally((e) -> {
            plugin.getLogger().severe("Failed to cleanup world container: " + e.getMessage());
            e.printStackTrace();
            return null;
        }).thenApplyAsync(toDelete -> {
            if (toDelete == null) return null;
            toDelete.forEach(path -> {
                try {
                    FileUtil.deleteFolder(path);
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to delete world folder " + path + ": " + e.getMessage());
                }
            });
            return null;
        });
    }

}
