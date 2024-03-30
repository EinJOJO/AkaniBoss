package it.einjojo.akani.boss.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;

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




    public boolean test(Entity entity) {
        return test(entity.getLocation());
    }

    public boolean test(Location location) {
        return location.getX() >= minX && location.getX() <= maxX &&
                location.getY() >= minY && location.getY() <= maxY &&
                location.getZ() >= minZ && location.getZ() <= maxZ;
    }


}
