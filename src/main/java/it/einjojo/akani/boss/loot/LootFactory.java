package it.einjojo.akani.boss.loot;

import org.bukkit.inventory.ItemStack;

public class LootFactory {

    public LootChancedItem item(ItemStack itemStack, float chance) {
        return null;
    }

    public LootFixedMoney money(int amount) {
        return null;
    }


    public boolean isVaultSupported() {
        return false;
    }

    public boolean isAkaniEconomySupported() {
        return false;
    }

}
