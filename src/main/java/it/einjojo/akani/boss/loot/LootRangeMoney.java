package it.einjojo.akani.boss.loot;

import it.einjojo.akani.boss.integration.economy.Economy;
import org.bukkit.entity.Player;

import java.util.Collection;

public record LootRangeMoney(int min, int max, Economy economy) implements Loot {


    @Override
    public void grant(Collection<Player> players) {

    }
}
