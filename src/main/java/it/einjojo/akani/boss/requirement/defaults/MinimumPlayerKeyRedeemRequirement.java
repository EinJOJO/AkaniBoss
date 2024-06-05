package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Stupid requirement that checks if a certain amount of players are nearby.
 */
public class MinimumPlayerKeyRedeemRequirement implements Requirement {
    public static final String NAME = "min_players";
    private final int minimumPlayers;
    private final double checkRadius;

    public MinimumPlayerKeyRedeemRequirement(int minimumPlayers, double checkRadius) {
        this.minimumPlayers = minimumPlayers;
        this.checkRadius = checkRadius;
    }


    @Override
    public String toString() {
        return NAME + ARGUMENT_SPLITTER + minimumPlayers + ARGUMENT_SPLITTER + checkRadius;
    }

    @Override
    public boolean check(Boss boss, Player player) {
        if (boss.keyRedeemLocation() == null) {
            return true;
        }
        return (boss.keyRedeemLocation().getNearbyEntitiesByType(Player.class, checkRadius).size() >= minimumPlayers);
    }

    @Override
    public Component denyMessage(Player player) {
        return Component.text("Es müssen mindestens " + minimumPlayers + " in der Nähe sein!");
    }

    @Override
    public CheckType checkType() {
        return CheckType.KEY_REDEEM;
    }
}
