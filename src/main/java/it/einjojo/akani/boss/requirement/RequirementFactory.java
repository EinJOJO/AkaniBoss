package it.einjojo.akani.boss.requirement;

import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.requirement.defaults.LevelRequirement;
import it.einjojo.akani.boss.requirement.defaults.MinimumPlayerKeyRedeemRequirement;
import it.einjojo.akani.boss.requirement.defaults.UnlockedWithKeyRequirement;

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

    public Requirement parseRequirement(String requirementString) {
        String[] args = SPLIT_PATTERN.split(requirementString);
        if (args.length == 0) {
            return null;
        }
        return switch (args[0]) {
            case LevelRequirement.NAME -> LevelRequirement.ofArgs(args);
            case MinimumPlayerKeyRedeemRequirement.NAME -> MinimumPlayerKeyRedeemRequirement.ofArgs(args);
            case UnlockedWithKeyRequirement.NAME -> unlockedWithKeyRequirement();
            default -> null;
        };
    }

}
