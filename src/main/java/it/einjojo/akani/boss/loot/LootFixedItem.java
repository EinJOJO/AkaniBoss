package it.einjojo.akani.boss.loot;

import it.einjojo.akani.boss.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

public record LootFixedItem(ItemStack itemStack) implements Loot {
    @Override
    public void grant(Collection<Player> players) {
        Iterator<Player> iterator = players.iterator();
        for (ItemStack stack : ItemUtil.splitItem(itemStack, players.size())) {
            if (stack == null) continue;
            Player player = iterator.next();
            player.getInventory().addItem(stack);
            player.sendMessage(MessageFormat.format("§8- §7{0}§8x§7{1}", stack.getAmount(), stack.getType()));

        }
    }
}
