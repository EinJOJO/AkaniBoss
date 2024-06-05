package it.einjojo.akani.boss.loot;

import it.einjojo.akani.boss.integration.economy.Economy;
import org.bukkit.inventory.ItemStack;

public class LootFactory {

    private final Economy economy;

    public LootFactory(Economy economy) {
        this.economy = economy;
    }

    public LootFixedItem createFixedItem(ItemStack itemStack) {
        return new LootFixedItem(itemStack);
    }

    public LootFixedMoney createFixedMoney(int amount) {
        return new LootFixedMoney(amount, economy);
    }

    public LootRangeMoney createRangeMoney(int min, int max) {
        return new LootRangeMoney(min, max, economy);
    }

    public LootChancedItem createChancedItem(ItemStack itemStack, float chance) {
        return new LootChancedItem(itemStack, chance);
    }

}
