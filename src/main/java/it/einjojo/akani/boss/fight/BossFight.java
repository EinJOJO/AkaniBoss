package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.UUID;

public class BossFight {

    private final Set<UUID> participants;

    public BossFight(Set<UUID> participants) {
        this.participants = participants;
    }

    public void addParticipant(UUID uuid) {
        participants.add(uuid);
    }

    public Set<UUID> participants() {
        return ImmutableSet.copyOf(participants);
    }
}
