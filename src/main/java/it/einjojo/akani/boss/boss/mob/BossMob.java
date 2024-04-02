package it.einjojo.akani.boss.boss.mob;

import it.einjojo.akani.boss.fight.BossFight;
import org.bukkit.Location;

public interface BossMob<T> {

    /**
     * @return the name of the mob
     */
    String mobName();

    /**
     * @param location the location where the mob should spawn
     * @return the spawned mob
     */
    T spawn(Location location);

    /**
     * @param fight the fight to check
     * @return true if the mob can spawn in the fight
     */
    boolean canSpawn(BossFight fight);

}
