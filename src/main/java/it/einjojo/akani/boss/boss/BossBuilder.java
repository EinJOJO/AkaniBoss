package it.einjojo.akani.boss.boss;


import it.einjojo.akani.boss.requirement.EntranceRequirement;
import it.einjojo.akani.boss.requirement.KeyReedemRequirement;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import it.einjojo.akani.boss.util.BoundaryBox;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BossBuilder {
    private String id;
    private String name;
    private BossDifficulty difficulty = BossDifficulty.NORMAL;
    private int level = 1;
    private Location keyRedeemLocation;
    private BoundaryBox entranceBox;
    private String roomTemplateName;
    private List<EntranceRequirement> entranceRequirements = List.of(
            RequirementFactory.keyUsedEntranceRequirement()
    );
    private List<KeyReedemRequirement> keyRedeemRequirements = List.of();
    private ItemStack keyItem;


    public BossBuilder() {
    }

    public BossBuilder(Boss boss) {
        this.id = boss.id();
        this.name = boss.bossName();
        this.difficulty = boss.difficulty();
        this.level = boss.bossLevel();
        this.keyRedeemLocation = boss.keyRedeemLocation();
        this.entranceBox = boss.entranceBox();
        this.roomTemplateName = boss.roomTemplateName();
        this.entranceRequirements = boss.entranceRequirements();
        this.keyRedeemRequirements = boss.keyRedeemRequirements();
    }

    public BossBuilder id(String id) {
        this.id = id;
        return this;
    }

    public BossBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BossBuilder difficulty(BossDifficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public BossBuilder level(int level) {
        this.level = level;
        return this;
    }

    public BossBuilder keyRedeemLocation(Location keyRedeemLocation) {
        this.keyRedeemLocation = keyRedeemLocation;
        return this;
    }

    public BossBuilder entranceBox(BoundaryBox entranceBox) {
        this.entranceBox = entranceBox;
        return this;
    }

    public BossBuilder roomTemplateName(String roomTemplateName) {
        this.roomTemplateName = roomTemplateName;
        return this;
    }

    public BossBuilder entranceRequirements(List<EntranceRequirement> entranceRequirements) {
        this.entranceRequirements = entranceRequirements;
        return this;
    }

    public BossBuilder keyRedeemRequirements(List<KeyReedemRequirement> keyRedeemRequirements) {
        this.keyRedeemRequirements = keyRedeemRequirements;
        return this;
    }

    public BossBuilder keyItem(ItemStack keyItem) {
        this.keyItem = keyItem;
        return this;
    }


    public Boss build() {
        if (id == null) throw new IllegalStateException("id is null");
        if (name == null) throw new IllegalStateException("name is null");
        if (roomTemplateName == null) throw new IllegalStateException("roomTemplateName is null");
        if (keyRedeemLocation == null) throw new IllegalStateException("keyRedeemLocation is null");
        if (entranceBox == null) throw new IllegalStateException("entranceBox is null");
        if (entranceRequirements == null) throw new IllegalStateException("entranceRequirements is null");
        if (keyRedeemRequirements == null) throw new IllegalStateException("keyReedem Requirements is null");
        if (keyItem == null) throw new IllegalStateException("keyItem is null");
        return new Boss(id, name, roomTemplateName, level, difficulty, keyRedeemLocation, entranceRequirements, keyRedeemRequirements, entranceBox, keyItem);
    }

}
