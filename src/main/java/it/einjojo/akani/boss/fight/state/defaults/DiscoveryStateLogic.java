package it.einjojo.akani.boss.fight.state.defaults;

import io.lumine.mythic.core.mobs.ActiveMob;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;
import it.einjojo.akani.boss.fight.BossMobRegistry;
import it.einjojo.akani.boss.fight.state.StateLogic;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class DiscoveryStateLogic implements StateLogic {
    private static final int SPAWN_DISTANCE_SQUARED = 64 * 64;
    private static final Logger log = LoggerFactory.getLogger(DiscoveryStateLogic.class);
    private final JavaPlugin plugin;
    private final BossMobRegistry bossMobRegistry;
    private final Location bossSpawnLocation;
    private final BossFight bossFight;
    private final BossBar findBossRoomBossBar;
    private UUID bossUuid;
    private boolean wasNearBossSpawn;
    private boolean hasSeenBoss;

    public DiscoveryStateLogic(JavaPlugin plugin, BossMobRegistry bossMobRegistry, BossFight bossFight) {
        this.plugin = plugin;
        this.bossMobRegistry = bossMobRegistry;
        this.bossFight = bossFight;
        findBossRoomBossBar = BossBar.bossBar(
                Component.text("Finde den Boss!", NamedTextColor.GREEN),
                BossBar.MAX_PROGRESS,
                BossBar.Color.PURPLE,
                BossBar.Overlay.NOTCHED_6
        );
        bossSpawnLocation = bossFight.fightRoom().bossSpawnLocation();

    }

    @Override
    public void tick() {
        if (!wasNearBossSpawn) {
            List<Player> players = bossFight.participantsPlayers();
            Player discoveryPlayer = findPlayerNearBossSpawn(players);
            if (discoveryPlayer != null) {
                wasNearBossSpawn = true;
                discoveryPlayer.playSound(discoveryPlayer, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.3f, 0.2f);
                spawnBoss();
            }
        } else if (!hasSeenBoss) {
            List<Player> players = bossFight.participantsPlayers();
            Player playerWhoLooksAtBoss = raytraceBoss(players);
            if (playerWhoLooksAtBoss != null) {
                hasSeenBoss = true;
                hideBossBar(players);
                announceBossDiscovery(players, playerWhoLooksAtBoss);
            }
        } else {
            bossFight.setState(BossFightState.FIGHTING);
        }
    }

    private Player raytraceBoss(List<Player> players) {
        for (Player player : players) {
            if (player.getWorld().equals(bossSpawnLocation.getWorld())) {
                RayTraceResult res = player.rayTraceEntities(40, false);
                if (res == null) continue;
                Entity entity = res.getHitEntity();
                if (entity == null) continue;
                if (bossUuid.equals(entity.getUniqueId())) {
                    return player;
                }
            }
        }
        return null;
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

    public BossBar findBossRoomBossBar() {
        return findBossRoomBossBar;
    }

    private Location bossSpawnLocation() {
        return bossSpawnLocation;
    }

    private void spawnBoss() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Object spawned = bossFight.boss().bossMob().spawn(bossSpawnLocation());
            if (spawned == null) {
                bossFight.setState(BossFightState.FATAL_EXCEPTION);
            }
            try {
                if (spawned instanceof Entity entity) {
                    bossUuid = entity.getUniqueId();
                } else if (spawned instanceof ActiveMob mythicMob) {
                    bossUuid = mythicMob.getEntity().getUniqueId();
                } else {
                    log.warn("Spawned Boss could not be added to registry");
                    bossFight.setState(BossFightState.FATAL_EXCEPTION);
                    return;
                }
                bossMobRegistry.register(bossUuid, bossFight);
            } catch (NoClassDefFoundError ignore) {
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
