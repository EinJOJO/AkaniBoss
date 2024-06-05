package it.einjojo.akani.boss.input.creator;

import it.einjojo.akani.boss.input.DropItemInput;
import it.einjojo.akani.boss.input.PlayerChatInput;
import it.einjojo.akani.boss.loot.Loot;
import it.einjojo.akani.boss.loot.LootFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class LootListCreator {
    private final JavaPlugin plugin;
    private final Consumer<List<Loot>> lootConsumer;
    private final List<Loot> loots = new LinkedList<>();
    private final LootFactory lootFactory;
    private Player player;

    public LootListCreator(JavaPlugin plugin, Player player, Consumer<List<Loot>> lootConsumer, LootFactory lootFactory) {
        this.plugin = plugin;
        this.player = player;
        this.lootConsumer = lootConsumer;
        this.lootFactory = lootFactory;
        setupItemLoop();
    }

    private void setupItemLoop() {
        sendMessage("§a1. §7Fixed Item");
        sendMessage("§a2. §7Chanced Item");
        sendMessage("§a3. §7Fixed Money");
        sendMessage("§a4. §7Chanced Money");
        sendMessage("§a5. §7Finish");
        sync(() -> {
            new PlayerChatInput(player, (input) -> {
                switch (input) {
                    case "1" -> sync(this::setupFixedItem);
                    case "2" -> sync(this::setupChancedItem);
                    case "3" -> sync(this::setupFixedMoney);
                    case "4" -> sync(this::setupChancedMoney);
                    case "5" -> sync(() -> {
                        player = null;
                        lootConsumer.accept(loots);
                    });
                    default -> {
                        sendMessage("Invalid input");
                        setupItemLoop();
                    }
                }
            }, this::onInputCancel);
        });
    }

    private void sync(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    private void setupFixedItem() {
        sendMessage("Droppe das Item, was als Belohnung gedropt werden soll.");
        new DropItemInput(player, (item) -> {

            sendMessage(MessageFormat.format("Added {0} x {1} with name {2} to list ", item.getAmount(), item.getType(), item.getItemMeta().getDisplayName()));
            loots.add(lootFactory.createFixedItem(item));
            setupItemLoop();
        }, this::onInputCancel);
    }

    private void setupChancedItem() {
        sendMessage("Droppe das Item, was als Belohnung gedropt werden soll.");
        new DropItemInput(player, (item) -> {
            sync(() -> {
                setupChancedItem(item);
            });
        }, this::onInputCancel);
    }

    private void setupChancedItem(ItemStack itemStack) {
        sendMessage("Enter chance: 0 bis 100");
        new PlayerChatInput(player, (input) -> {
            try {
                sendMessage(MessageFormat.format("§7Added §a{0} §7x §a{1} §7with name {2} §7to list with chance §e{3}% ", itemStack.getAmount(), itemStack.getType(), itemStack.getItemMeta().getDisplayName(), input));
                loots.add(lootFactory.createChancedItem(itemStack, Float.parseFloat(input) / 100));
                setupItemLoop();
            } catch (NumberFormatException e) {
                sendMessage("Invalid input");
                setupChancedItem(itemStack);
            }
        }, this::onInputCancel);
    }

    private void setupFixedMoney() {
        sendMessage("Enter amount");
        new PlayerChatInput(player, (input) -> {
            try {
                loots.add(lootFactory.createFixedMoney(Integer.parseInt(input)));
                sendMessage(MessageFormat.format("§7Added §a{0} §7money", input));
                setupItemLoop();
            } catch (NumberFormatException e) {
                sendMessage("Invalid input");
                setupFixedMoney();
            }
        }, this::onInputCancel);
    }

    private void setupChancedMoney() {
        sendMessage("Enter amount");
        new PlayerChatInput(player, (input) -> {
            try {
                sync(() -> {
                    setupMaximum(Integer.parseInt(input));
                });
            } catch (NumberFormatException e) {
                sendMessage("Invalid input");
                sync(this::setupChancedMoney);
            }
        }, this::onInputCancel);
    }

    private void setupMaximum(int amount) {
        sendMessage("Enter maximum money");
        new PlayerChatInput(player, (input) -> {
            try {
                loots.add(lootFactory.createRangeMoney(amount, Integer.parseInt(input)));
                sendMessage(MessageFormat.format("§7Added ranged money: §a{0} §7to §a{1}", amount, input));
                setupItemLoop();
            } catch (NumberFormatException e) {
                sendMessage("Invalid input");
                sync(() -> {
                    setupMaximum(amount);
                });
            }
        }, this::onInputCancel);

    }

    private void sendMessage(String message) {
        player.sendMessage("§8[§cLOOT-INPUT§8] §7" + message);
    }

    private void onInputCancel() {
        setupItemLoop();
    }


}
