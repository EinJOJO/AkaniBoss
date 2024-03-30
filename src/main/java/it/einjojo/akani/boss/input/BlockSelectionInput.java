package it.einjojo.akani.boss.input;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class BlockSelectionInput implements Input<Block> {
    private static final Component INSTRUCTIONS = MiniMessage.miniMessage().deserialize("<gray>Wähle ein Block aus durch Zerstören oder schreibe <white>cancel<gray> um abzubrechen.");
    private final Consumer<Block> blockConsumer;
    private final Runnable onCancel;
    private final UUID playerUUID;

    public BlockSelectionInput(Player player, Consumer<Block> blockConsumer) {
        this(player, blockConsumer, () -> {
        });
    }

    public BlockSelectionInput(Player player, Consumer<Block> blockConsumer, Runnable onCancel) {
        this.blockConsumer = blockConsumer;
        this.playerUUID = player.getUniqueId();
        this.onCancel = onCancel;
        player.sendMessage(INSTRUCTIONS);
        player.sendActionBar(INSTRUCTIONS);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
        register();
    }


    @Override
    public UUID playerUniqueId() {
        return playerUUID;
    }

    @Override
    public Consumer<Block> callback() {
        return blockConsumer;
    }

    @Override
    public void cancel() {
        onCancel.run();
    }


}
