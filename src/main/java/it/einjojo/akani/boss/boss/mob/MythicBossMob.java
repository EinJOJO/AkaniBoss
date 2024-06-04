package it.einjojo.akani.boss.boss.mob;

import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import it.einjojo.akani.boss.fight.BossFight;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record MythicBossMob(@NotNull MythicMob mythicMob, MobManager mobManager,
                            BossMobFactory<?> factory) implements BossMob<ActiveMob> {

    @Override
    public String mobName() {
        return mythicMob.getInternalName();
    }

    @NotNull
    @Override
    public ActiveMob spawn(@NotNull Location location) {
        return mythicMob.spawn(BukkitAdapter.adapt(location), 1);
    }

    @Override
    public void despawn(UUID mobUuid) {
        for (ActiveMob activeMob : mobManager.getActiveMobs()) {
            if (activeMob.getUniqueId().equals(mobUuid)) {
                activeMob.despawn();
            }
        }
    }

    @Override
    public boolean canSpawn(BossFight fight) {
        return true;
    }


}
