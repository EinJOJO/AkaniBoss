package it.einjojo.akani.boss.input;

import com.google.common.base.Preconditions;
import it.einjojo.akani.boss.room.ActiveRoom;
import it.einjojo.akani.boss.room.RoomData;
import it.einjojo.akani.boss.room.RoomManager;
import it.einjojo.akani.boss.room.RoomTemplate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoomCreator {
    private static final int MAX_STEPS = 2;
    private final RoomManager roomManager;
    private final JavaPlugin plugin;
    private final RoomTemplate roomTemplate;
    private final RoomData.Builder roomDataBuilder;
    private final Runnable onCompletion;
    private Player player;
    private int stage = 0;
    private ActiveRoom activeRoom;

    public RoomCreator(@NotNull RoomManager roomManager, @NotNull JavaPlugin plugin, @NotNull Player player, @NotNull RoomTemplate roomTemplate, @Nullable Runnable onCompletion) {
        Preconditions.checkNotNull(roomManager);
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(roomTemplate);
        Preconditions.checkNotNull(player);

        this.roomManager = roomManager;
        this.plugin = plugin;
        this.roomTemplate = roomTemplate;
        this.player = player;
        this.onCompletion = onCompletion;
        this.roomDataBuilder = RoomData.builder();
    }


    public void startSetup() {
        if (stage != 0) throw new IllegalStateException("RoomCreator is already running");
        roomManager.createRoomByTemplate(roomTemplate).thenAcceptAsync((created -> {
                    activeRoom = created;
                    player.teleportAsync(created.world().getSpawnLocation()).thenRun(this::routine);
                }), Bukkit.getScheduler().getMainThreadExecutor(plugin))
                .exceptionally(throwable -> {
                    plugin.getLogger().severe("Exception in room creator: " + throwable.getMessage());
                    return null;
                });
    }

    private void routine() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (hasCompleted()) {
                RoomTemplate newTemplate = new RoomTemplate(roomTemplate.templateName(), roomTemplate.worldTemplateFolder(), roomDataBuilder.build());
                roomManager.updateRoomTemplate(newTemplate);
                close();
                if (onCompletion != null) {
                    onCompletion.run();
                }
                return;
            }
            switch (stage) {
                case 0 -> { // Set Player Spawn
                    sendMessage("Setze den Spieler Spawn!");
                    new BlockSelectionInput(player, block -> {
                        sendMessage("Spieler spawn gesetzt");
                        roomDataBuilder.playerSpawnLocation(block.getLocation().add(0, 1, 0));
                        stage++;
                        routine();
                    });
                }
                case 1 -> {
                    sendMessage("Setze den Boss Spawn!");
                    new BlockSelectionInput(player, block -> {
                        sendMessage("Bossspawn gesetzt");
                        roomDataBuilder.bossSpawnLocation(block.getLocation().add(0, 1, 0));
                        stage++;
                        routine();
                    });
                }
                default -> throw new IllegalStateException("Invalid stage: " + stage);
            }

        });
    }

    private void sendMessage(String message) {
        player.sendMessage(message);
    }

    public void close() {
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        player = null;
        roomManager.deleteActiveRoom(activeRoom);
        activeRoom = null;

    }

    public boolean hasCompleted() {
        return stage >= MAX_STEPS;
    }

}
