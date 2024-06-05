package it.einjojo.akani.boss.boss.mob;

import it.einjojo.akani.boss.fight.BossFight;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @param <T> Entity type of the mob when spawned
 */
public interface BossMob<T> {

    /**
     * @return the name of the mob
     */
    String mobName();


    /**
     * @param location the location where the mob should spawn
     * @return the spawned mob or null if exception occurred
     */
    @Nullable
    T spawn(@NotNull Location location);

    /**
     * @param fight the fight to check
     * @return true if the mob can spawn in the fight
     */
    boolean canSpawn(BossFight fight);

    void despawn(UUID mobUuid);

    /**
     * Factory to create the mob
     *
     * @return the factory
     */
    BossMobFactory<?> factory();

}
