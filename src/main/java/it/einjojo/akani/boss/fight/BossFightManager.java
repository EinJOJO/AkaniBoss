package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BossFightManager {
    //private static final Duration BOSS_ROOM_UNSEAL_DURATION = Duration.ofSeconds(30);
    private final BossSystem bossSystem;
    private final List<BossFight> activeBossFights;
    private final Map<UUID, String> entrancePermissions; // Player-UUID -> BossId

    public BossFightManager(BossSystem bossSystem) {
        this.bossSystem = bossSystem;
        this.entrancePermissions = new HashMap<>();
        this.activeBossFights = new ArrayList<>();
    }

    public void permitPlayer(UUID player, String bossId) {
        entrancePermissions.put(player, bossId);
    }

    public boolean canEnterBossRoom(UUID player, String bossId) {
        return entrancePermissions.get(player) == null || entrancePermissions.get(player).equals(bossId);
    }


    public BossFight startNewBossFight(Boss target) {
        BossFight fight = new BossFight(target, bossSystem);
        activeBossFights.add(fight);
        return fight;
    }


    public List<BossFight> activeBossFights() {
        return ImmutableList.copyOf(activeBossFights);
    }
}

