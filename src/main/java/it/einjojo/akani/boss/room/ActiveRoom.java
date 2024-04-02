package it.einjojo.akani.boss.room;

import it.einjojo.akani.boss.util.FileUtil;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a room that is currently active in the game. This means that the room is created from a template and is currently used by a player.
 *
 * @param roomID   The unique ID of the room
 * @param template The template of the room
 */
public record ActiveRoom(UUID roomID, RoomTemplate template) {
    public static String WORLD_NAME_PREFIX = "boss_";

    /**
     * @return Whether the room is currently used by a player
     */
    @Deprecated(forRemoval = true)
    public boolean isEmpty() {
        return false;
    } //TODO Remove this method

    public String worldName() {
        return WORLD_NAME_PREFIX + roomID.toString();
    }

    public World world() {
        return Bukkit.getWorld(worldName());
    }

    public Path worldFolder() {
        return Bukkit.getWorldContainer().toPath().resolve(worldName() + "/");
    }

    public CompletableFuture<World> createWorld(JavaPlugin plugin) {
        CompletableFuture<World> future = new CompletableFuture<>();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Path destination = worldFolder();
                Files.copy(template.worldTemplateFolder(), destination);
                plugin.getLogger().info("Template World copied to world folder");
            } catch (IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                World world = new WorldCreator(worldName()).generateStructures(false).type(WorldType.FLAT).createWorld();
                if (world == null) {
                    future.completeExceptionally(new IllegalStateException("World is null after creation."));
                    return;
                }
                // Apply default gamerules.
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                world.setGameRule(GameRule.DO_FIRE_TICK, false);
                world.setGameRule(GameRule.DO_MOB_LOOT, false);
                world.setGameRule(GameRule.DO_TILE_DROPS, false);
                world.setGameRule(GameRule.DO_VINES_SPREAD, false);
                world.setGameRule(GameRule.MOB_GRIEFING, false);
                world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
                world.setAutoSave(false);
                plugin.getLogger().info("World created successfully by template.");
                future.complete(world);
            });
        });
        return future;
    }

    /**
     * Unloads and deletes the world
     *
     * @throws IllegalStateException if players are in the world
     * @throws IOException           if the world cannot be deleted
     */
    public void deleteWorld() throws IOException {
        if (!world().getPlayers().isEmpty())
            throw new IllegalStateException("Cannot delete world while players are in it");
        Bukkit.unloadWorld(world(), false);
        FileUtil.deleteFolder(worldFolder());
    }
}