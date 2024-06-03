package it.einjojo.akani.boss.fight.state.defaults;

import it.einjojo.akani.boss.BossSystemPlugin;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;
import it.einjojo.akani.boss.fight.state.StateLogic;
import it.einjojo.akani.boss.room.ActiveRoom;
import it.einjojo.akani.boss.room.RoomManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class PrepareStateLogic implements StateLogic {
    private static final Logger log = LoggerFactory.getLogger(PrepareStateLogic.class);
    private final BossFight bossFight;
    private final CompletableFuture<ActiveRoom> activeRoomCompletableFuture;

    public PrepareStateLogic(BossFight bossFight, RoomManager roomManager) {
        this.bossFight = bossFight;
        activeRoomCompletableFuture = roomManager.createRoomByTemplate(bossFight.boss().roomTemplateName()).whenComplete(((activeRoom, throwable) -> {
            if (throwable != null) {
                log.error("Failed to create room", throwable);
            }
            if (activeRoom != null) {
                onRoomCreation(activeRoom);
            }
        }));
    }


    @Override
    public void tick() {
        if (!activeRoomCompletableFuture.isDone()) {
            for (Player player : bossFight.participantsPlayers()) {
                player.sendActionBar(MiniMessage.miniMessage().deserialize("<gray><i>Die Welt wird errichtet..."));
            }
        }
    }

    public void onRoomCreation(ActiveRoom activeRoom) {
        bossFight.setRoom(activeRoom);
        bossFight.setState(BossFightState.INTRODUCTION);
        JavaPlugin plugin = JavaPlugin.getPlugin(BossSystemPlugin.class);
        Location location = activeRoom.template().roomData().playerSpawnLocation().clone();
        location.setWorld(activeRoom.world());
        activeRoom.world().setSpawnLocation(location);
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player player : bossFight.participantsPlayers()) {
                player.sendMessage(Component.text("Welt wurde erstellt", NamedTextColor.GRAY));
                player.teleportAsync(location);
            }
        });

    }



}
