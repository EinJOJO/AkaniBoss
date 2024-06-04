package it.einjojo.akani.boss.loot;

import org.bukkit.entity.Player;

public interface Loot {
    String id();

    void grant(Player player);

}
