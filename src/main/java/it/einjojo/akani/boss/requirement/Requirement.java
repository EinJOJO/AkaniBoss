package it.einjojo.akani.boss.requirement;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Requirement {
    /**
     * @param player the player to check.
     * @return true if the player meets the requirement, false otherwise.
     */
    boolean check(Player player);

    Component denyMessage();

}
