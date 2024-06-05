package it.einjojo.akani.boss.loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Iterator;

public record LootFixedItem(ItemStack itemStack) implements Loot {
    @Override
    public void grant(Collection<Player> players) {
        Iterator<Player> iterator = players.iterator();
        for (ItemStack stack : new ItemStack[players.size()]) {
            if (stack == null) continue;
            iterator.next().getInventory().addItem(stack);
        }
    }
}
