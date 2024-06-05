package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class LevelRequirement implements Requirement {
    public static final String NAME = "min_level";
    private final int min_level;

    public LevelRequirement(int minLevel) {
        min_level = minLevel;
    }

    @Override
    public boolean check(Boss boss, Player player) {
        return player.getLevel() >= min_level;
    }

    @Override
    public Component denyMessage(Player player) {
        return Component.text("Du musst mindestens Level " + min_level + " sein!");
    }

    @Override
    public String toString() {
        return NAME + ARGUMENT_SPLITTER + min_level;
    }
}
