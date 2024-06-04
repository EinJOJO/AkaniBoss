package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.state.defaults.StateLogicFactoryImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Boss Fight Manager
 * - Manages all active boss fights
 * - Manages entrance permissions
 * - Manages player -> boss fights
 */
public class BossFightManager {
    private static final Duration ENTRANCE_TIMEOUT = Duration.ofSeconds(30);
    private final BossSystem bossSystem;
    private final BossMobRegistry bossMobRegistry = new BossMobRegistry();
    private final List<BossFight> activeBossFights = new LinkedList<>();
    private final List<EntrancePermission> entrancePermissionList = new LinkedList<>();
    private final Map<UUID, BossFight> playerBossFights = new ConcurrentHashMap<>();
    private BossFight.Listener defaultChangeListener;


    public BossFightManager(BossSystem bossSystem) {
        this.bossSystem = bossSystem;
    }


    public BossFight.Listener defaultChangeListener() {
        return defaultChangeListener;
    }

    public void setDefaultChangeListener(BossFight.Listener defaultChangeListener) {
        this.defaultChangeListener = defaultChangeListener;
        for (BossFight fight : activeBossFights) {
            fight.setChangeListener(defaultChangeListener);
        }
    }

    public void clearEntrance(UUID uuid, Boss boss) {
        entrancePermissionList.removeIf(permission -> permission.uuid().equals(uuid) && permission.boss().equals(boss));
    }

    public void allowEntrance(UUID uuid, Boss boss) {
        entrancePermissionList.add(new EntrancePermission(uuid, boss, System.currentTimeMillis() + ENTRANCE_TIMEOUT.toMillis()));
    }


    public boolean isAllowedToEnter(UUID uuid, Boss boss) {
        for (EntrancePermission permission : entrancePermissionList) {
            if (permission.isExpired()) {
                entrancePermissionList.remove(permission);
            } else if (permission.uuid().equals(uuid) && permission.boss().equals(boss)) {
                return true;
            }
        }
        return false;
    }


    protected void setPlayerBossFight(UUID uuid, @Nullable BossFight fight) {
        if (fight == null) {
            playerBossFights.remove(uuid);
            return;
        }
        if (playerBossFights.containsKey(uuid)) {
            throw new IllegalStateException("Player already has a boss fight");
        }
        playerBossFights.put(uuid, fight);
    }

    public BossFight playerBossFight(UUID uuid) {
        return playerBossFights.get(uuid);
    }


    public BossFight startNewBossFight(Boss target) {
        BossFight fight = new BossFight(this, new StateLogicFactoryImpl(bossSystem.plugin(), bossSystem.roomManager(), bossMobRegistry), target);
        fight.setChangeListener(defaultChangeListener);
        activeBossFights.add(fight);
        return fight;
    }

    /**
     * Deletes the boss fight and the room belonging to it.
     *
     * @param fight the fight
     */
    @Blocking
    public void closeBossFight(BossFight fight) {
        Bukkit.getScheduler().runTask(bossSystem.plugin(), () -> {
            fight.setState(BossFightState.CLOSED);
            World fightWorld = fight.fightRoom().world();
            Location location = fight.boss().keyRedeemLocation();
            for (Player playerInWorld : fightWorld.getPlayers()) { // Regard Players that are spectating but not in fight
                playerInWorld.teleportAsync(location);
            }
            for (Player player : fight.participantsPlayers()) {
                fight.removeParticipant(player.getUniqueId());
                activeBossFights.remove(fight);
                UUID mobUUID = bossMobRegistry.getByFight(fight);
                bossMobRegistry.unregister(fight);
                fight.boss().bossMob().despawn(mobUUID);
            }
        });
        Bukkit.getScheduler().runTaskLater(bossSystem.plugin(), () -> {
            bossSystem.roomManager().deleteActiveRoom(fight.fightRoom());
        }, 20 * 10);

    }


    public List<BossFight> activeBossFights() {
        return ImmutableList.copyOf(activeBossFights);
    }

    public BossSystem bossSystem() {
        return bossSystem;
    }

    public BossMobRegistry bossMobRegistry() {
        return bossMobRegistry;
    }

}

