package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.Boss;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BossFightManager {
    private static final Duration BOSS_ROOM_UNSEAL_DURATION = Duration.ofSeconds(30);
    private final AkaniBoss akaniBoss;
    private final List<BossFight> activeBossFights;
    private final Map<String, Instant> unsealedBossRooms; // BossId -> Expiry

    public BossFightManager(AkaniBoss akaniBoss) {
        this.akaniBoss = akaniBoss;
        this.unsealedBossRooms = new HashMap<>();
        this.activeBossFights = new ArrayList<>();
    }

    public boolean isUnsealed(String bossId) {
        Instant instant = unsealedBossRooms.get(bossId);
        if (instant == null) {
            return false;
        }
        if (instant.isBefore(Instant.now())) {
            seal(bossId);
            return false;
        }
        return true;
    }

    public void seal(String bossId) {
        unsealedBossRooms.remove(bossId);
    }

    /**
     * Called when a player goes into the dungeon and meets the entrance requirements.
     */
    public void participateInFight(UUID uuid, Boss boss) {
        BossFight fight = activeBossFights.stream()
                .filter(bossFight -> bossFight.boss().id().equals(boss.id()) && (bossFight.state().equals(BossFightState.PREPARING) || bossFight.state().equals(BossFightState.INTRODUCTION))).findFirst().orElse(null);
        if (fight == null) {
            fight = new BossFight(boss, akaniBoss);
            activeBossFights.add(fight);
        }
        fight.addParticipant(uuid);
    }

    public void unsealBoss(Boss boss, UUID unsealer) {
        if (isUnsealed(boss.id())) return;
        unsealedBossRooms.put(boss.id(), Instant.now().plus(BOSS_ROOM_UNSEAL_DURATION));
        akaniBoss.roomManager().emptyRoom(boss.roomTemplateName()).whenComplete((room, throwable) -> {
            if (throwable != null) {
                akaniBoss.plugin().getLogger().warning("Failed to unseal room " + boss.roomTemplateName() + " for boss " + boss.id());
                return;
            }
            akaniBoss.plugin().getLogger().info("Unsealed room " + boss.roomTemplateName() + " for boss " + boss.id());

        });
    }

    public void startBossFight(Boss target) {
        activeBossFights.add(new BossFight(target, akaniBoss));
    }

    public AkaniBoss akaniBoss() {
        return akaniBoss;
    }

    public List<BossFight> activeBossFights() {
        return ImmutableList.copyOf(activeBossFights);
    }
}

