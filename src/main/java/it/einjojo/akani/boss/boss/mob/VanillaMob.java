package it.einjojo.akani.boss.boss.mob;

import it.einjojo.akani.boss.fight.BossFight;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record VanillaMob(EntityType entityType, BossMobFactory<?> factory) implements BossMob<Entity> {
    @Override
    public String mobName() {
        return entityType.name();
    }

    @Override
    public void despawn(UUID mobUuid) {

    }

    @NotNull
    @Override
    public Entity spawn(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("provided Location's World cannot be null");
        }
        return world.spawnEntity(location, entityType);
    }

    @Override
    public boolean canSpawn(BossFight fight) {
        return true;
    }
}
