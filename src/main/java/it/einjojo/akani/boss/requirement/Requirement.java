package it.einjojo.akani.boss.requirement;

import it.einjojo.akani.boss.boss.Boss;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Requirement {

    String requirementName();

    /**
     * @param boss   the boss that has the requirement.
     * @param player the player to check.
     * @return true if the player meets the requirement, false otherwise.
     */
    boolean check(Boss boss, Player player);

    /**
     * @param player the player that does not meet the requirement.
     * @return the message to send to the player.
     */
    Component denyMessage(Player player);

}
