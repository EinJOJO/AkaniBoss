package it.einjojo.akani.boss.room;

import org.bukkit.Location;

/**
 * Represents the data of a room like the boss, the loot, the location, etc.
 */
public record RoomData(Location playerSpawnLocation, Location bossSpawnLocation) {
    public static final RoomData PLACEHOLDER = new RoomData(new Location(null, 0, 0, 0), new Location(null, 0, 0, 0));

    public static class Builder {
        private Location playerSpawnLocation;
        private Location bossSpawnLocation;

        public Builder playerSpawnLocation(Location playerSpawnLocation) {
            this.playerSpawnLocation = playerSpawnLocation;
            return this;
        }

        public Builder bossSpawnLocation(Location bossSpawnLocation) {
            this.bossSpawnLocation = bossSpawnLocation;
            return this;
        }

        public RoomData build() {
            return new RoomData(playerSpawnLocation, bossSpawnLocation);
        }
    }
}
