package it.einjojo.akani.boss.loot;

import it.einjojo.akani.boss.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

public record LootChancedItem(@NotNull ItemStack itemStack, @Range(from = 0, to = 1) float chance) implements Loot {
    private static final SecureRandom random = new SecureRandom();

    @Override
    public void grant(Collection<Player> players) {
        if (random.nextFloat() < chance) {
            Iterator<Player> iterator = players.iterator();
            for (ItemStack itemStack : ItemUtil.splitItem(itemStack, players.size())) {
                if (itemStack == null) continue;
                Player player = iterator.next();
                player.getInventory().addItem(itemStack);
                player.sendMessage(MessageFormat.format("§8- §7{0}§8x§7{1}", itemStack.getAmount(), itemStack.getType()));
            }
        }
    }
}
