package it.einjojo.akani.boss.boss;

import com.google.common.collect.ImmutableMap;
import it.einjojo.akani.boss.util.HologramUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BossManager {
    private final Map<String, Boss> bosses = new HashMap<>();
    private final Map<Location, String> keyLocToBossIDMap = new HashMap<>();

    public void registerBoss(Boss boss) {
        bosses.put(boss.id(), boss);
        keyLocToBossIDMap.put(boss.keyRedeemLocation(), boss.id());
        HologramUtil.createBossHologram(boss.keyRedeemLocation().clone().add(0, 3, 0), boss);
    }

    public Map<String, Boss> bosses() {
        return ImmutableMap.copyOf(bosses);
    }

    @Nullable
    public Boss boss(String id) {
        return bosses.get(id);
    }

    public boolean isBossKeyLocation(Location location) {
        return keyLocToBossIDMap.containsKey(location);
    }

    public void saveBoss(Boss boss) {

    }

    public void loadBoss(String id) {

    }

    public void loadAll() {

    }

}
