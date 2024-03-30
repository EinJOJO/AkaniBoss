package it.einjojo.akani.boss.world;

import it.einjojo.akani.boss.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class RoomManager {
    private final JavaPlugin plugin;
    private final List<Room> loadedRooms = new LinkedList<>();
    private final Map<String, RoomTemplate> roomTemplates = new HashMap<>();

    public RoomManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    /**
     * Creates or gets a room by the room template
     *
     * @param roomTemplateName The name of the room template
     * @return A free room
     */
    public CompletableFuture<Room> emptyRoom(String roomTemplateName) {
        for (Room room : loadedRooms()) {
            if (room.isEmpty() && room.template().templateName().equals(roomTemplateName)) {
                return CompletableFuture.completedFuture(room);
            }
        }
        return createRoomByTemplate(roomTemplateName);
    }

    public CompletableFuture<Room> createRoomByTemplate(String templateName) {
        RoomTemplate template = roomTemplates.get(templateName);
        if (template == null) throw new IllegalArgumentException("Room Template not found");
        Room room = new Room(UUID.randomUUID(), template);
        CompletableFuture<Room> future = new CompletableFuture<>();
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

    public List<Room> loadedRooms() {
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
                stream.filter(path -> path.getFileName().toString().startsWith(Room.WORLD_NAME_PREFIX)).forEach(worldPath -> {
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

    public void loadTemplates() {

    }
}
