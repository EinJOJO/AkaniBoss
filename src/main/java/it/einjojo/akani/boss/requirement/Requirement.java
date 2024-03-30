package it.einjojo.akani.boss.requirement;

import it.einjojo.akani.boss.boss.Boss;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Represents a requirement that a player must meet to enter a boss room.
 * <p> Gets checked when a player tries to use a key to enter a boss room.
 * <p> or when he enters the Boss-Room</p>
 */
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

    default CheckType checkType() {
        return CheckType.BOTH;
    }

    public enum CheckType {
        KEY_REDEEM,
        ENTER_ROOM,
        BOTH,
    }

}
