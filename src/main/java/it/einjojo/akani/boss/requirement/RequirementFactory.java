package it.einjojo.akani.boss.requirement;

import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.requirement.defaults.KeyUsedEntranceRequirement;
import it.einjojo.akani.boss.requirement.defaults.LevelRequirement;
import it.einjojo.akani.boss.requirement.defaults.MinimumPlayerKeyRedeemRequirement;

public class RequirementFactory {
    private static KeyUsedEntranceRequirement keyUsedEntranceRequirement;
    private final BossFightManager bossFightManager;

    public RequirementFactory(BossFightManager bossFightManager) {
        this.bossFightManager = bossFightManager;
        keyUsedEntranceRequirement = new KeyUsedEntranceRequirement(bossFightManager);
    }

    public static KeyUsedEntranceRequirement keyUsedEntranceRequirement() {
        return keyUsedEntranceRequirement;
    }


    /**
     * Creates a new key redeem requirement from a string.
     *
     * @param string the string to parse
     * @return the key redeem requirement
     * @throws IllegalArgumentException if no key redeem requirement with the given name exists
     */
    public KeyReedemRequirement createKeyRedeemRequirement(String string) {
        String[] args = string.split(":");
        if (args[0].equals(MinimumPlayerKeyRedeemRequirement.NAME)) {
            return MinimumPlayerKeyRedeemRequirement.ofArgs(args);
        }
        if (args[0].equals(LevelRequirement.NAME)) {
            return LevelRequirement.ofArgs(args);
        }
        throw new IllegalArgumentException("Invalid key redeem requirement: " + args[0]);
    }

    public EntranceRequirement createEntranceRequirement(String string) {
        String[] args = string.split(":");
        if (args[0].equals(KeyUsedEntranceRequirement.NAME)) {
            return keyUsedEntranceRequirement();
        }
        if (args[0].equals(LevelRequirement.NAME)) {
            return LevelRequirement.ofArgs(args);
        }
        throw new IllegalArgumentException("Invalid entrance requirement: " + args[0]);
    }
}
