package it.einjojo.akani.boss.listener;

import it.einjojo.akani.boss.room.ActiveRoom;
import it.einjojo.akani.boss.room.RoomManager;
import it.einjojo.akani.boss.room.RoomTemplate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class RoomListener implements Listener {

    private final RoomManager roomManager;
    private final JavaPlugin plugin;

    public RoomListener(RoomManager roomManager, JavaPlugin plugin) {
        this.roomManager = roomManager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        for (ActiveRoom activeRoom : roomManager.loadedRooms()) {
            String worldName = event.getWorld().getName();
            if (!roomManager.isWorldNameToBeDeleted(worldName)) return;
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                try {
                    activeRoom.deleteWorldFolder();
                } catch (IOException e) {
                    plugin.getSLF4JLogger().error("Failed to delete world folder", e);
                }
            }, 10);

        }
    }

    @EventHandler
    public void notifyRoomSetup(PlayerJoinEvent event) {
        for (RoomTemplate template : roomManager.roomTemplatesRequiringSetup()) {
            event.getPlayer().sendMessage("Room " + template.templateName() + " muss noch eingerichtet werden!");
        }
    }
}
