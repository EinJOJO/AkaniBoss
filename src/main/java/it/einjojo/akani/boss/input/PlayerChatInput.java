package it.einjojo.akani.boss.input;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerChatInput implements Input<String> {
    private static final Component INSTRUCTIONS = MiniMessage.miniMessage().deserialize("<gray>Schreibe eine Nachricht in den Chat oder schreibe <white>cancel<gray> um abzubrechen.");
    private final UUID playerUniqueId;
    private final Consumer<String> callback;
    private final Runnable onCancel;

    public PlayerChatInput(Player player, Consumer<String> callback) {
        this(player, callback, null);
    }

    public PlayerChatInput(Player player, Consumer<String> callback, Runnable onCancel) {
        this.playerUniqueId = player.getUniqueId();
        this.callback = callback;
        this.onCancel = onCancel;
        player.sendActionBar(INSTRUCTIONS);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
        register();
    }

    @Override
    public UUID playerUniqueId() {
        return playerUniqueId;
    }

    @Override
    public Consumer<String> callback() {
        return callback;
    }

    @Override
    public void cancel() {
        if (onCancel != null) {
            onCancel.run();
        }
    }
}
