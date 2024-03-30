package it.einjojo.akani.boss.fight;

import it.einjojo.akani.boss.boss.Boss;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class BossFightManager {
    private static final Duration BOSS_ROOM_UNSEAL_DURATION = Duration.ofSeconds(20);
    private final Map<String, Instant> unsealedBossRooms; // BossId -> Expiry

    public BossFightManager() {
        this.unsealedBossRooms = new HashMap<>();
    }

    public boolean isUnsealed(String bossId) {
        Instant instant = unsealedBossRooms.get(bossId);
        if (instant == null) {
            return false;
        }
        if (instant.isBefore(Instant.now())) {
            unsealedBossRooms.remove(bossId);
            return false;
        }
        return true;
    }

    public void unsealBoss(Boss boss) {
        unsealedBossRooms.put(boss.id(), Instant.now().plus(BOSS_ROOM_UNSEAL_DURATION));

    }

}

