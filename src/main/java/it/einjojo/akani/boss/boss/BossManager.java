package it.einjojo.akani.boss.boss;

import com.google.common.collect.ImmutableMap;
import it.einjojo.akani.boss.storage.BossStorage;
import it.einjojo.akani.boss.storage.StorageException;
import it.einjojo.akani.boss.util.HologramUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossManager {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BossManager.class);
    private final Map<String, Boss> bosses = new HashMap<>();
    private final Map<Location, String> keyLocToBossIDMap = new HashMap<>();
    private final BossStorage storage;


    public BossManager(BossStorage storage) {
        this.storage = storage;
    }

    public void registerBoss(Boss boss) {
        bosses.put(boss.id(), boss);
        keyLocToBossIDMap.put(boss.keyRedeemLocation(), boss.id());
        HologramUtil.createBossHologram(boss.keyRedeemLocation().clone().toCenterLocation().add(0, 3, 0), boss);
        logger.info("Registered boss {}", boss.id());
    }

    public void unregisterBoss(Boss boss) {
        bosses.remove(boss.id());
        keyLocToBossIDMap.remove(boss.keyRedeemLocation());
        HologramUtil.removeBossHologram(boss);
        logger.info("Unregistered boss {}", boss.id());
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

    @Nullable
    public Boss bossByLocation(Location keyLocation) {
        String bossID = keyLocToBossIDMap.get(keyLocation);
        if (bossID == null) {
            return null;
        }
        return boss(bossID);

    }

    public boolean saveBoss(Boss boss) {
        logger.info("Saving boss {}", boss.id());
        try {
            storage.saveBoss(boss);
            return true;
        } catch (StorageException e) {
            logger.error("Failed to save boss {}", boss.id(), e);
            return false;
        }
    }

    public void load(String id) {
        Boss existing = boss(id);
        if (existing != null) {
            unregisterBoss(existing);
        }
        try {
            Boss boss = storage.loadBoss(id);
            if (boss != null) {
                registerBoss(boss);
            }
        } catch (Exception e) {
            logger.error("Failed to load boss {}", id, e);
        }
    }


    public void loadAll() {
        for (Boss boss : bosses.values()) {
            unregisterBoss(boss);
        }
        try {
            List<Boss> bosses = storage.loadAllBosses();
            for (Boss boss : bosses) {
                if (boss == null) continue;
                registerBoss(boss);

            }
            logger.info("Loaded {} bosses", bosses.size());
        } catch (StorageException e) {
            logger.error("Failed to load bosses", e);
        }


    }

}
