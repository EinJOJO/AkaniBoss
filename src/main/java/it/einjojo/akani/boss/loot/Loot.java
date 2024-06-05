package it.einjojo.akani.boss.loot;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface Loot {

    /**
     * Called when a player defeats a boss and the loot is granted
     *
     * @param players players who defeated the boss. Might be more than one if the boss was defeated by multiple players
     */
    void grant(Collection<Player> players);

}
