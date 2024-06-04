package it.einjojo.akani.boss.loot;

import org.bukkit.entity.Player;

public record LootRangeMoney(int min, int max) implements Loot {
    @Override
    public String id() {
        return "ranged_money";
    }

    @Override
    public void grant(Player player) {

    }


}
