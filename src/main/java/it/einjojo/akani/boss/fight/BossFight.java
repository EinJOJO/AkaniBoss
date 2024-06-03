package it.einjojo.akani.boss.fight;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.state.StateLogic;
import it.einjojo.akani.boss.fight.state.StateLogicFactory;
import it.einjojo.akani.boss.room.ActiveRoom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

public class BossFight {
    @NotNull
    private final BossFightManager bossFightManager;
    @NotNull
    private final Boss boss;
    @NotNull
    private final Set<UUID> participants = new HashSet<>();
    @NotNull
    private final Instant startedAt = Instant.now();
    @NotNull
    private BossFightState state;
    @NotNull
    private StateLogic stateLogic;
    @Nullable
    private ActiveRoom fightRoom;


    public BossFight(@NotNull BossFightManager bossFightManager, @NotNull Boss boss) {
        Preconditions.checkNotNull(bossFightManager);
        Preconditions.checkNotNull(boss);
        this.bossFightManager = bossFightManager;
        this.boss = boss;
        state = BossFightState.PREPARING;
        stateLogic = new StateLogicFactory().createLogic(this);
    }

    /**
     * Sets the room of the fight.
     *
     * @param room the room to set
     * @throws IllegalStateException if fight is not in preparing state
     */
    public void setRoom(ActiveRoom room) {
        if (!state.equals(BossFightState.PREPARING)) {
            throw new IllegalStateException("Cannot set room when fight is not in preparing state");
        }
        this.fightRoom = room;
    }


    public void addParticipant(Player player) {
        UUID uuid = player.getUniqueId();
        bossFightManager.setPlayerBossFight(uuid, this);
        participants.add(uuid);
        stateLogic.onParticipantJoin(player);

    }

    public List<Player> participantsPlayers() {
        List<Player> players = new LinkedList<>();
        for (UUID participantUUID : participants) {
            Player player = Bukkit.getPlayer(participantUUID);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    public void setState(BossFightState state) {
        this.state = state;
        stateLogic = new StateLogicFactory().createLogic(this);
    }


    public void tick() {
        stateLogic.tick();
    }

    public Boss boss() {
        return boss;
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
