package it.einjojo.akani.boss.input.creator;

import it.einjojo.akani.boss.loot.Loot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class LootListCreator implements Closeable {
    private final JavaPlugin plugin;
    private final Consumer<List<Loot>> lootConsumer;
    private final List<Loot> loots = new LinkedList<>();
    private Player player;

    public LootListCreator(JavaPlugin plugin, Player player, Consumer<List<Loot>> lootConsumer) {
        this.plugin = plugin;
        this.player = player;
        this.lootConsumer = lootConsumer;
        setupItemLoop();
    }

    private void setupItemLoop() {

    }

    private void sendMessage(String message) {

    }

    private void onInputCancel() {

    }


    @Override
    public void close() {
        player = null;
    }
}
