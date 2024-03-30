package it.einjojo.akani.boss.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public record BoundaryBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    public static BoundaryBox of(Location loc1, Location loc2) {
        return new BoundaryBox(
                Math.min(loc1.getX(), loc2.getX()),
                Math.min(loc1.getY(), loc2.getY()),
                Math.min(loc1.getZ(), loc2.getZ()),
                Math.max(loc1.getX(), loc2.getX()),
                Math.max(loc1.getY(), loc2.getY()),
                Math.max(loc1.getZ(), loc2.getZ())
        );
    }

    boolean test(Entity entity) {
        return entity.getLocation().getX() >= minX && entity.getLocation().getX() <= maxX &&
                entity.getLocation().getY() >= minY && entity.getLocation().getY() <= maxY &&
                entity.getLocation().getZ() >= minZ && entity.getLocation().getZ() <= maxZ;
    }


}
