package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossFightManager {
    private static final Duration ENTRANCE_TIMEOUT = Duration.ofSeconds(30);
    private final BossSystem bossSystem;
    private final List<BossFight> activeBossFights = new LinkedList<>();
    private final List<EntrancePermission> entrancePermissionList = new LinkedList<>();
    private final Map<UUID, BossFight> playerBossFights = new ConcurrentHashMap<>();


    public BossFightManager(BossSystem bossSystem) {
        this.bossSystem = bossSystem;
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


    protected void setPlayerBossFight(UUID uuid, BossFight fight) {
        if (playerBossFights.containsKey(uuid)) {
            throw new IllegalStateException("Player already has a boss fight");
        }
        playerBossFights.put(uuid, fight);
    }

    public BossFight playerBossFight(UUID uuid) {
        return playerBossFights.get(uuid);
    }


    public BossFight startNewBossFight(Boss target) {
        BossFight fight = new BossFight(this, target);
        activeBossFights.add(fight);
        return fight;
    }


    public List<BossFight> activeBossFights() {
        return ImmutableList.copyOf(activeBossFights);
    }
}

