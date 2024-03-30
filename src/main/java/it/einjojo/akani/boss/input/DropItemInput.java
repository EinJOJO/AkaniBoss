package it.einjojo.akani.boss.input;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

public class DropItemInput implements Input<ItemStack> {
    private static final Component INSTRUCTION = MiniMessage.miniMessage().deserialize("<blue>[ItemInput] <gray>Droppe ein Item oder schreibe <red>cancel.");
    private final UUID playerUniqueId;
    private final Consumer<ItemStack> callback;
    private final Runnable onCancel;

    public DropItemInput(Player player, Consumer<ItemStack> callback) {
        this(player, callback, () -> {
        });
    }

    public DropItemInput(Player player, Consumer<ItemStack> callback, Runnable cancelCallback) {
        this.playerUniqueId = player.getUniqueId();
        this.callback = callback;
        this.onCancel = cancelCallback;
        player.sendMessage(INSTRUCTION);
        register();
    }


    @Override
    public UUID playerUniqueId() {
        return playerUniqueId;
    }

    @Override
    public Consumer<ItemStack> callback() {
        return callback;
    }

    @Override
    public void cancel() {
        onCancel.run();
    }
}
