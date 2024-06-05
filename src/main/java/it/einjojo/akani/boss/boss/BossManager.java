package it.einjojo.akani.boss.boss;

import com.google.common.collect.ImmutableMap;
import it.einjojo.akani.boss.storage.BossStorage;
import it.einjojo.akani.boss.storage.StorageException;
import it.einjojo.akani.boss.util.HologramUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<Boolean> saveBoss(Boss boss) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Saving boss {}", boss.id());
            try {
                storage.saveBoss(boss);
            } catch (StorageException e) {
                throw new RuntimeException(e);
            }
            return true;
        }).exceptionally(throwable -> {
            logger.error("Failed to save boss {}", boss.id());
            throwable.fillInStackTrace();
            return false;
        });
    }

    public CompletableFuture<Boss> load(String id) {
        Boss existing = boss(id);
        if (existing != null) {
            unregisterBoss(existing);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                return storage.loadBoss(id);
            } catch (StorageException e) {
                throw new RuntimeException(e);
            }
        }).thenApply(boss -> {
            if (boss != null) {
                registerBoss(boss);
            }
            return boss;
        }).exceptionally(throwable -> {
            logger.error("Failed to load boss {}", id);
            throwable.fillInStackTrace();
            return null;
        });
    }


    public CompletableFuture<Boolean> loadAll() {
        for (Boss boss : bosses.values()) {
            unregisterBoss(boss);
        }
        return CompletableFuture.supplyAsync(() -> {
            try {
                return storage.loadAllBosses();
            } catch (StorageException e) {
                throw new RuntimeException(e);
            }
        }).thenApply((list) -> {
            list.forEach(this::registerBoss);
            logger.info("Loaded {} bosses", list.size());
            return true;
        }).exceptionally(throwable -> {
            logger.error("Failed to load bosses");
            throwable.fillInStackTrace();
            return false;
        });

    }

}
