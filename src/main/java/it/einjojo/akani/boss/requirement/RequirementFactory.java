package it.einjojo.akani.boss.requirement;

import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.requirement.defaults.LevelRequirement;
import it.einjojo.akani.boss.requirement.defaults.MinimumPlayerKeyRedeemRequirement;
import it.einjojo.akani.boss.requirement.defaults.UnlockedWithKeyRequirement;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class RequirementFactory {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("(?<!\\\\):");
    private static UnlockedWithKeyRequirement unlockedWithKeyRequirement;

    public RequirementFactory(BossFightManager bossFightManager) {
        unlockedWithKeyRequirement = new UnlockedWithKeyRequirement(bossFightManager);
    }

    public static UnlockedWithKeyRequirement unlockedWithKeyRequirement() {
        if (unlockedWithKeyRequirement == null) {
            throw new IllegalStateException("UnlockedWithKeyRequirement not initialized");
        }
        return unlockedWithKeyRequirement;
    }

    @Nullable
    public Requirement parseRequirement(String requirementString) {
        String[] args = SPLIT_PATTERN.split(requirementString);
        if (args.length == 0) {
            return null;
        }
        return switch (args[0]) {
            case UnlockedWithKeyRequirement.NAME -> unlockedWithKeyRequirement();
            case LevelRequirement.NAME -> createLevelRequirement(args);
            case MinimumPlayerKeyRedeemRequirement.NAME -> createMinimumPlayerKeyRedeemRequirement(args);
            default -> null;
        };
    }

    public LevelRequirement createLevelRequirement(String[] args) {
        int minLevel = args.length >= 1 ? Integer.parseInt(args[1]) : 0;
        return new LevelRequirement(minLevel);
    }

    public MinimumPlayerKeyRedeemRequirement createMinimumPlayerKeyRedeemRequirement(String[] args) {
        if (args.length < 2) {
            return null;
        }
        int minPlayers = Integer.parseInt(args[1]);
        double radius = args.length > 2 ? Double.parseDouble(args[2]) : 10;
        return new MinimumPlayerKeyRedeemRequirement(minPlayers, radius);
    }


}
