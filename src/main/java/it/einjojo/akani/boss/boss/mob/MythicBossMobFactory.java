package it.einjojo.akani.boss.boss.mob;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;

public class MythicBossMobFactory implements BossMobFactory<ActiveMob> {
    private final MobExecutor mythicMobs = MythicBukkit.inst().getMobManager();

    /**
     * @param name the name of the mob
     * @return a new {@link MythicBossMob}
     * @throws IllegalArgumentException if the name is not a valid EntityType
     */
    @Override
    public BossMob<ActiveMob> createBossMob(String name) {
        MythicMob mob = mythicMobs.getMythicMob(name).orElseThrow(() -> new IllegalArgumentException("Mythic Mob not found: " + name));
        return new MythicBossMob(mob, mythicMobs, this);
    }
}
