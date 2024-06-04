package it.einjojo.akani.boss.boss.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class VanillaMobFactory implements BossMobFactory<Entity> {
    /**
     * @param name the name of the mob
     * @return a new {@link VanillaMob}
     * @throws IllegalArgumentException if the name is not a valid EntityType
     */
    @Override

    public BossMob<Entity> createBossMob(String name) {
        return new VanillaMob(EntityType.valueOf(name), this);
    }
}
