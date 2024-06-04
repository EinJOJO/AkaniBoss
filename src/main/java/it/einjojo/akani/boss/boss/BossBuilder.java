package it.einjojo.akani.boss.boss;


import it.einjojo.akani.boss.boss.mob.BossMob;
import it.einjojo.akani.boss.requirement.Requirement;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class BossBuilder {
    private String id;
    private String name;
    private BossDifficulty difficulty = BossDifficulty.NORMAL;
    private int level = 1;
    private Location keyRedeemLocation;
    private BoundingBox entranceBox;
    private String roomTemplateName;
    private List<Requirement> requirements = new ArrayList<>(List.of(RequirementFactory.unlockedWithKeyRequirement()));
    private ItemStack keyItem;
    private BossMob<?> bossMob;


    public BossBuilder() {
    }

    public BossBuilder(Boss boss) {
        this.id = boss.id();
        this.name = boss.bossName();
        this.difficulty = boss.difficulty();
        this.level = boss.bossLevel();
        this.keyRedeemLocation = boss.keyRedeemLocation();
        this.entranceBox = boss.dungeonEntrance();
        this.roomTemplateName = boss.roomTemplateName();
        this.requirements = boss.requirements();
        this.keyItem = boss.keyItem();
        this.bossMob = boss.bossMob();
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

    public BossBuilder entranceBox(BoundingBox entranceBox) {
        this.entranceBox = entranceBox;
        return this;
    }

    public BossBuilder roomTemplateName(String roomTemplateName) {
        this.roomTemplateName = roomTemplateName;
        return this;
    }

    public BossBuilder addRequirement(Requirement requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public BossBuilder requirements(List<Requirement> entranceRequirements) {
        this.requirements = entranceRequirements;
        return this;
    }


    public BossBuilder keyItem(ItemStack keyItem) {
        this.keyItem = keyItem;
        return this;
    }

    public BossBuilder bossMob(BossMob<?> bossMob) {
        this.bossMob = bossMob;
        return this;
    }

    public Boss build() {
        if (id == null) throw new IllegalStateException("id is null");
        if (name == null) throw new IllegalStateException("name is null");
        if (roomTemplateName == null) throw new IllegalStateException("roomTemplateName is null");
        if (keyRedeemLocation == null) throw new IllegalStateException("keyRedeemLocation is null");
        if (entranceBox == null) throw new IllegalStateException("dungeonEntrance is null");
        if (requirements == null) throw new IllegalStateException("requirements is null");
        if (keyItem == null) throw new IllegalStateException("keyItem is null");
        if (bossMob == null) throw new IllegalStateException("bossMob is null");
        return new Boss(id, name, roomTemplateName, level, difficulty, keyRedeemLocation, requirements, entranceBox, keyItem, bossMob);
    }

}
