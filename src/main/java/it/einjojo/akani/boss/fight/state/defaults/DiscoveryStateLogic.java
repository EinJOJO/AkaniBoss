package it.einjojo.akani.boss.fight.state.defaults;

import io.lumine.mythic.core.mobs.ActiveMob;
import it.einjojo.akani.boss.BossSystemPlugin;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;
import it.einjojo.akani.boss.fight.BossMobRegistry;
import it.einjojo.akani.boss.fight.state.StateLogic;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiscoveryStateLogic implements StateLogic {

    private static final int SPAWN_DISTANCE_SQUARED = 42 * 42;
    private static final Logger log = LoggerFactory.getLogger(DiscoveryStateLogic.class);
    private final BossMobRegistry bossMobRegistry;
    private final Location bossSpawnLocation;
    private final BossFight bossFight;
    private final BossBar findBossRoomBossBar;
    private boolean wasNearBossSpawn;

    public DiscoveryStateLogic(BossMobRegistry bossMobRegistry, BossFight bossFight) {
        this.bossMobRegistry = bossMobRegistry;
        this.bossFight = bossFight;
        findBossRoomBossBar = BossBar.bossBar(
                Component.text("Finde den Boss!", NamedTextColor.GREEN),
                BossBar.MAX_PROGRESS,
                BossBar.Color.PURPLE,
                BossBar.Overlay.NOTCHED_6
        );
        bossSpawnLocation = bossFight.fightRoom().template().roomData().bossSpawnLocation().clone();
        bossSpawnLocation.setWorld(bossFight.fightRoom().world());

    }

    @Override
    public void tick() {
        if (!wasNearBossSpawn) {
            List<Player> players = bossFight.participantsPlayers();
            Player discoveryPlayer = findPlayerNearBossSpawn(players);
            if (discoveryPlayer != null) {
                wasNearBossSpawn = true;
                hideBossBar(players);
                spawnBoss();
                announceBossDiscovery(players, discoveryPlayer);
            }
        } else {
            //TODO: Mehr.
            bossFight.setState(BossFightState.FIGHTING);
        }
    }

    private void announceBossDiscovery(List<Player> players, Player discoveryPlayer) {
        for (Player player : players) {
            player.sendMessage(Component.text("Der Boss wurde von %s gefunden!".formatted(discoveryPlayer.getName()), NamedTextColor.GREEN));
        }
    }

    public void onDisable() {
        List<Player> participants = (bossFight).participantsPlayers();
        hideBossBar(participants);
    }


    public void hideBossBar(List<Player> players) {
        for (Player player : players) {
            player.hideBossBar(findBossRoomBossBar);
        }
    }

    private Location bossSpawnLocation() {
        return bossSpawnLocation;
    }

    private void spawnBoss() {
        Bukkit.getScheduler().runTask(BossSystemPlugin.instance(), () -> {
            Object spawned = bossFight.boss().bossMob().spawn(bossSpawnLocation());
            if (spawned instanceof ActiveMob mythicMob) {
                bossMobRegistry.register(mythicMob.getParentUUID().get(), bossFight);
            } else if (spawned instanceof Entity entity) {
                bossMobRegistry.register(entity.getUniqueId(), bossFight);
            } else {
                log.warn("Spawned Boss could not be added to registry ");
            }
        });
    }

    private @Nullable Player findPlayerNearBossSpawn(List<Player> players) {
        Location bossSpawn = bossSpawnLocation();
        for (Player player : players) {
            player.showBossBar(findBossRoomBossBar);
            if (!(player.getLocation().distanceSquared(bossSpawn) < SPAWN_DISTANCE_SQUARED)) continue;
            return player;
        }
        return null;
    }
}
