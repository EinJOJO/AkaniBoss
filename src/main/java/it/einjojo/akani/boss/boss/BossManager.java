package it.einjojo.akani.boss.boss;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class BossManager {
    private final Map<String, Boss> bosses = new HashMap<>();
    private final Map<Location, String> keyLocToBossIDMap = new HashMap<>();

    public void registerBoss(Boss boss) {
        bosses.put(boss.id(), boss);
        keyLocToBossIDMap.put(boss.keyRedeemLocation(), boss.id());
    }

    public Boss boss(String id) {
        return bosses.get(id);
    }

    public boolean isBossKeyLocation(Location location) {
        return keyLocToBossIDMap.containsKey(location);
    }
    

    public void load(String id) {

    }

    public void loadAll() {

    }

}
