package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.requirement.EntranceRequirement;
import it.einjojo.akani.boss.requirement.KeyReedemRequirement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class LevelRequirement implements KeyReedemRequirement, EntranceRequirement {
    public static final String NAME = "min_level";
    private final int min_level;

    public LevelRequirement(int minLevel) {
        min_level = minLevel;
    }

    public static LevelRequirement ofArgs(String[] args) {
        int minLevel = args.length >= 1 ? Integer.parseInt(args[1]) : 0;
        return new LevelRequirement(minLevel);
    }

    @Override
    public String requirementName() {
        return "level";
    }

    @Override
    public boolean check(Boss boss, Player player) {
        return player.getLevel() >= min_level;
    }

    @Override
    public Component denyMessage(Player player) {
        return Component.text("You need to be at least level " + min_level + " to enter this boss room!");
    }

    @Override
    public String toString() {
        return NAME + ":" + min_level;
    }
}
