package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableSet;
import it.einjojo.akani.boss.boss.Boss;

import java.util.Set;
import java.util.UUID;

public class BossFight {
    private final Boss boss;
    private final Set<UUID> participants;

    public BossFight(Boss boss, Set<UUID> participants) {
        this.boss = boss;
        this.participants = participants;
    }

    public void addParticipant(UUID uuid) {
        participants.add(uuid);
    }

    public Set<UUID> participants() {
        return ImmutableSet.copyOf(participants);
    }
}
