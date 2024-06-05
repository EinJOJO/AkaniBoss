package it.einjojo.akani.boss.util;

import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    public static ItemStack[] splitItem(ItemStack itemStack, int split) {
        ItemStack[] result = new ItemStack[split];
        int totalQuantity = itemStack.getAmount();
        int quotient = totalQuantity / split;
        int remainder = totalQuantity % split;
        for (int i = 0; i < split; i++) {
            ItemStack splitted = itemStack.clone();
            if (i < remainder) {
                splitted.setAmount(quotient + 1);
                result[i] = splitted;
            } else if (quotient > 0) {
                splitted.setAmount(quotient);
                result[i] = splitted;
            } else {
                result[i] = null;
            }
        }
        return result;
    }

}
