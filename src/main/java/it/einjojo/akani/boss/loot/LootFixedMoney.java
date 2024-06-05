package it.einjojo.akani.boss.loot;

import it.einjojo.akani.boss.integration.economy.Economy;
import org.bukkit.entity.Player;

import java.util.Collection;

public record LootFixedMoney(int amount, Economy economy) implements Loot {


    @Override
    public void grant(Collection<Player> players) {

    }
}
