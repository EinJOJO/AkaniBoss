package it.einjojo.akani.boss.loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record LootChancedItem(ItemStack item, float chance) implements Loot {
    @Override
    public String id() {
        return "item";
    }

    @Override
    public void grant(Player player) {

    }
}
