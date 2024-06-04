package it.einjojo.akani.boss.loot;

import org.bukkit.entity.Player;

public record LootFixedMoney(int amount) implements Loot {
    @Override
    public String id() {
        return "money";
    }

    @Override
    public void grant(Player player) {

    }
}
