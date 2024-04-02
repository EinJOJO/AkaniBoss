package it.einjojo.akani.boss.loot;

import org.bukkit.inventory.ItemStack;

public record LootItem(ItemStack item, double chance) {
}
