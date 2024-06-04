package it.einjojo.akani.boss.fight;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossMobRegistry {

    private final Map<UUID, BossFight> mobBossFights = new ConcurrentHashMap<>();
    private final Map<BossFight, UUID> bossFightsMobs = new ConcurrentHashMap<>();

    public void register(UUID mobUuid, BossFight fight) {
        mobBossFights.put(mobUuid, fight);
        bossFightsMobs.put(fight, mobUuid);
    }

    public void unregister(UUID mobUuid) {
        BossFight fightRemoved = mobBossFights.remove(mobUuid);
        if (fightRemoved != null) {
            bossFightsMobs.remove(fightRemoved);
        }
    }

    public void unregister(BossFight bossFight) {
        UUID mobUuid = bossFightsMobs.remove(bossFight);
        if (mobUuid != null) {
            mobBossFights.remove(mobUuid);
        }
    }

    public @Nullable BossFight getByMobId(UUID mobUuid) {
        return mobBossFights.get(mobUuid);
    }

    public @Nullable UUID getByFight(BossFight fight) {
        return bossFightsMobs.get(fight);
    }


}
