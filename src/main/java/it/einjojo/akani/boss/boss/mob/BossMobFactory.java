package it.einjojo.akani.boss.boss.mob;

import io.lumine.mythic.core.mobs.ActiveMob;

public interface BossMobFactory<T> {

    static VanillaMobFactory vanilla() {
        return new VanillaMobFactory();
    }

    static BossMobFactory<ActiveMob> mythic() {
        return new MythicBossMobFactory();
    }

    /**
     * Creates a {@link BossMob} based on the specified name.
     *
     * @param name the name of the mob
     * @return a new {@link BossMob}
     * @throws IllegalArgumentException if the name is not a valid EntityType
     */
    BossMob<T> createBossMob(String name);
}
