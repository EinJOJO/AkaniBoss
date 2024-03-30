package it.einjojo.akani.boss.boss;

import it.einjojo.akani.boss.requirement.EntranceRequirement;
import it.einjojo.akani.boss.requirement.KeyReedemRequirement;
import net.kyori.adventure.text.Component;

import java.util.List;

public class Boss {
    private final List<EntranceRequirement> entranceRequirements;
    private final List<KeyReedemRequirement> keyRedeemRequirements;
    private Component bossName;
    private int bossLevel;
    private BossDifficulty difficulty;

    public Boss(List<EntranceRequirement> entranceRequirements, List<KeyReedemRequirement> keyRedeemRequirements, Component bossName, int bossLevel, BossDifficulty difficulty) {
        this.entranceRequirements = entranceRequirements;
        this.keyRedeemRequirements = keyRedeemRequirements;
        this.bossName = bossName;
        this.bossLevel = bossLevel;
        this.difficulty = difficulty;
    }


    public List<EntranceRequirement> entranceRequirements() {
        return entranceRequirements;
    }

    public List<KeyReedemRequirement> keyRedeemRequirements() {
        return keyRedeemRequirements;
    }

    public void setBossName(Component bossName) {
        this.bossName = bossName;
    }

    public void setBossLevel(int bossLevel) {
        this.bossLevel = bossLevel;
    }

    public void setDifficulty(BossDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Component bossName() {
        return bossName;
    }

    public int bossLevel() {
        return bossLevel;
    }

    public BossDifficulty difficulty() {
        return difficulty;
    }

}
