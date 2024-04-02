package it.einjojo.akani.boss.room;

import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.storage.jsonfile.JsonRoomDataFile;
import it.einjojo.akani.boss.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class RoomManager {
    private final JavaPlugin plugin;
    private final AkaniBoss internal;
    private final Path templatesFolder;
    private final List<ActiveRoom> loadedRooms = new LinkedList<>();
    private final Map<String, RoomTemplate> roomTemplates = new HashMap<>();

    public RoomManager(JavaPlugin plugin, AkaniBoss internal) {
        this.plugin = plugin;
        templatesFolder = plugin.getDataFolder().toPath().resolve("templates");
        this.internal = internal;
    }


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

    /**
     * Creates or gets a room by the room template
     *
     * @param roomTemplateName The name of the room template
     * @return A free room
     */
    public CompletableFuture<ActiveRoom> emptyRoom(String roomTemplateName) {
        ActiveRoom emptyLoadedRoom = emptyLoadedRoomByTemplate(roomTemplateName);
        if (emptyLoadedRoom != null) {
            return CompletableFuture.completedFuture(emptyLoadedRoom);
        }
        return createRoomByTemplate(roomTemplateName);
    }

    public ActiveRoom emptyLoadedRoomByTemplate(String s) {
        for (ActiveRoom room : loadedRooms) {
            if (room.isEmpty() && room.template().templateName().equals(s)) {
                return room;
            }
        }
        return null;
    }

    public CompletableFuture<ActiveRoom> createRoomByTemplate(String templateName) {
        RoomTemplate template = roomTemplates.get(templateName);
        if (template == null) throw new IllegalArgumentException("Room Template not found");
        ActiveRoom room = new ActiveRoom(UUID.randomUUID(), template);
        CompletableFuture<ActiveRoom> future = new CompletableFuture<>();
        room.createWorld(plugin).whenComplete((world, throwable) -> {
            if (throwable != null) {
                plugin.getLogger().severe("Failed to create world for room " + templateName + ": " + throwable.getMessage());
                future.completeExceptionally(throwable);
                return;
            }
            loadedRooms.add(room);
            future.complete(room);
        });
        return future;
    }


    public @Nullable RoomTemplate roomTemplate(String templateName) {
        return roomTemplates.get(templateName);
    }

    public List<ActiveRoom> loadedRooms() {
        return loadedRooms;
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
        }).thenApplyAsync(toDelete -> {
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
