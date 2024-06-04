package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.state.defaults.StateLogicFactoryImpl;
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


    public BossFightManager(BossSystem bossSystem) {
        this.bossSystem = bossSystem;
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
        BossFight fight = new BossFight(this, new StateLogicFactoryImpl(bossSystem.roomManager(), bossMobRegistry), target);
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
        fight.setState(BossFightState.CLOSED);
        for (Player player : fight.participantsPlayers()) {
            player.teleport(fight.boss().keyRedeemLocation());
            fight.removeParticipant(player.getUniqueId());
        }
        activeBossFights.remove(fight);
        UUID mobUUID = bossMobRegistry.getByFight(fight);
        bossMobRegistry.unregister(fight);
        fight.boss().bossMob().despawn(mobUUID);
        if (bossSystem.roomManager().deleteActiveRoom(fight.fightRoom())) {
        }
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

