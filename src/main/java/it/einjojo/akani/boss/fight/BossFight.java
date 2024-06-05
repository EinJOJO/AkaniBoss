package it.einjojo.akani.boss.fight;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import it.einjojo.akani.boss.BossSystemPlugin;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.state.StateLogic;
import it.einjojo.akani.boss.fight.state.StateLogicFactory;
import it.einjojo.akani.boss.fight.state.defaults.DiscoveryStateLogic;
import it.einjojo.akani.boss.room.ActiveRoom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BossFight {
    @NotNull
    private final BossFightManager bossFightManager;
    @NotNull
    private final StateLogicFactory stateLogicFactory;
    @NotNull
    private final Boss boss;
    @NotNull
    private final List<UUID> participants = new LinkedList<>();
    private final List<UUID> allParticipants = new LinkedList<>();

    private final long startedAt = System.currentTimeMillis();
    @Nullable
    private transient Listener changeListener;
    @NotNull
    private BossFightState state;
    @NotNull
    private StateLogic stateLogic;
    @Nullable
    private ActiveRoom fightRoom;

    public BossFight(@NotNull BossFightManager bossFightManager, @NotNull StateLogicFactory stateLogicFactory, @NotNull Boss boss) {
        Preconditions.checkNotNull(bossFightManager);
        Preconditions.checkNotNull(stateLogicFactory);
        Preconditions.checkNotNull(boss);
        this.bossFightManager = bossFightManager;
        this.stateLogicFactory = stateLogicFactory;
        this.boss = boss;
        state = BossFightState.PREPARING;
        stateLogic = stateLogicFactory.createLogic(this);
    }

    public boolean isRunning() {
        if (state.equals(BossFightState.INTRODUCTION)) {
            return true;
        } else if (state.equals(BossFightState.DISCOVERY)) {
            return true;
        }
        return state.equals(BossFightState.FIGHTING);

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
        try {
            bossFightManager.setPlayerBossFight(uuid, this);
            participants.add(uuid);
            allParticipants.add(uuid);
            if (changeListener != null) {
                changeListener.onParticipantJoin(this, player);
            }
            if (state != BossFightState.PREPARING) {
                teleportPlayerToSpawnOrParticipant(player);
            }
        } catch (Exception ex) {
            removeParticipant(uuid);
        }
    }

    /**
     * Participants that have participated. They might be dead already.
     *
     * @return the participants
     */
    public List<UUID> allParticipants() {
        return ImmutableList.copyOf(allParticipants);
    }

    public void teleportPlayerToSpawnOrParticipant(Player player) {
        JavaPlugin plugin = JavaPlugin.getPlugin(BossSystemPlugin.class);
        Bukkit.getScheduler().runTask(plugin, () -> {
            Location spawnLocation = fightRoom().world().getSpawnLocation();
            if (participants().isEmpty()) {
                player.teleportAsync(spawnLocation);
            } else {
                UUID target = getFirstNonMatchingParticipant(player.getUniqueId());
                Player targetPlayer = target != null ? Bukkit.getPlayer(target) : null;
                Location teleportLocation = targetPlayer != null ? targetPlayer.getLocation() : spawnLocation;
                player.teleportAsync(teleportLocation);
            }
        });
    }


    public UUID getFirstNonMatchingParticipant(UUID uuid) {
        return participants().stream()
                .filter(puuid -> !puuid.equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void removeParticipant(UUID uuid) {
        participants.remove(uuid);
        bossFightManager.setPlayerBossFight(uuid, null);
        if (changeListener != null) {
            changeListener.onParticipantLeave(this, uuid);
        }
    }

    public List<Player> allParticipantPlayers() {
        return playersFromUUID(allParticipants);
    }

    public List<Player> participantsPlayers() {
        return playersFromUUID(participants);
    }

    private List<Player> playersFromUUID(Collection<UUID> uuids) {
        List<Player> players = new LinkedList<>();
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    public void setState(BossFightState state) {
        this.state = state;
        stateLogic.disable();
        stateLogic = stateLogicFactory.createLogic(this);
        if (changeListener != null) {
            switch (state) {
                case VICTORY -> changeListener.onVictory(this);
                case DEFEATED -> changeListener.onDefeat(this);
            }
        }
    }

    public boolean isParticipant(UUID uuid) {
        return participants.contains(uuid);
    }

    public boolean isParticipant(Player player) {
        return participants.contains(player.getUniqueId());
    }

    public void tick() {
        stateLogic.tick();
    }

    public Boss boss() {
        return boss;
    }


    public long startedAt() {
        return startedAt;
    }

    public BossFightState state() {
        return state;
    }

    public ActiveRoom fightRoom() {
        return fightRoom;
    }

    public List<UUID> participants() {
        return ImmutableList.copyOf(participants);
    }

    public void setChangeListener(@Nullable Listener changeListener) {
        this.changeListener = changeListener;
    }

    @NotNull
    public BossFightManager bossFightManager() {
        return bossFightManager;
    }

    @NotNull
    public StateLogic stateLogic() {
        return stateLogic;
    }

    @Nullable
    public Listener changeListener() {
        return changeListener;
    }

    public interface Listener {
        void onParticipantJoin(BossFight fight, Player player);

        void onParticipantLeave(BossFight fight, UUID uuid);

        void onVictory(BossFight fight);

        void onDefeat(BossFight fight);
    }

}
