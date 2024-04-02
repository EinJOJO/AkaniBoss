package it.einjojo.akani.boss.fight;

import com.google.common.collect.ImmutableSet;
import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.room.ActiveRoom;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BossFight {

    private final Boss boss;
    private final AkaniBoss internal;
    private final Set<UUID> participants;
    private final Instant startedAt;
    private BossFightState state;
    private ActiveRoom fightRoom;


    public BossFight(Boss boss, AkaniBoss internal) {
        this.boss = boss;
        this.internal = internal;
        setState(BossFightState.PREPARING);
        this.participants = new HashSet<>();
        startedAt = Instant.now();
    }

    public void setRoom(ActiveRoom room) {
        if (!state.equals(BossFightState.PREPARING)) {
            throw new IllegalStateException("Cannot set room when fight is not in preparing state");
        }
        this.fightRoom = room;
        setState(BossFightState.INTRODUCTION);
    }


    public void addParticipant(UUID uuid) {
        participants.add(uuid);
    }

    public void setState(BossFightState state) {
        this.state = state;

    }


    public void update() {
        state.update(this);
    }

    public Boss boss() {
        return boss;
    }

    public AkaniBoss internal() {
        return internal;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public BossFightState state() {
        return state;
    }

    public ActiveRoom fightRoom() {
        return fightRoom;
    }

    public Set<UUID> participants() {
        return ImmutableSet.copyOf(participants);
    }
}
