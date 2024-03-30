package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.requirement.KeyReedemRequirement;
import it.einjojo.akani.boss.util.ParseUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MinimumPlayerKeyRedeemRequirement implements KeyReedemRequirement {
    public static String NAME = "min_players";
    private final int minimumPlayers;
    private final double checkRadius;

    public MinimumPlayerKeyRedeemRequirement(int minimumPlayers, double checkRadius) {
        this.minimumPlayers = minimumPlayers;
        this.checkRadius = checkRadius;
    }

    public static MinimumPlayerKeyRedeemRequirement ofArgs(String[] args) {
        if (args.length < 2) {
            return null;
        }
        int minPlayers = ParseUtil.parseInt("min_players", args[1]);
        double radius = args.length > 2 ? ParseUtil.parseDouble("radius", args[2]) : 10;
        return new MinimumPlayerKeyRedeemRequirement(minPlayers, radius);
    }

    @Override
    public String requirementName() {
        return NAME;
    }

    @Override
    public String toString() {
        return NAME + ":" + minimumPlayers + ":" + checkRadius;
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
        return Component.text("You need at least " + minimumPlayers + " players to redeem this key!");
    }
}
