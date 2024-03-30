package it.einjojo.akani.boss.boss;

import com.google.common.collect.ImmutableMap;
import it.einjojo.akani.boss.storage.BossStorage;
import it.einjojo.akani.boss.util.HologramUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class BossManager {
    private final Map<String, Boss> bosses = new HashMap<>();
    private final Map<Location, String> keyLocToBossIDMap = new HashMap<>();
    private final BossStorage storage;
    private final Logger logger = Logger.getLogger("BossManager");

    public BossManager(BossStorage storage) {
        this.storage = storage;
    }

    public void registerBoss(Boss boss) {
        bosses.put(boss.id(), boss);
        keyLocToBossIDMap.put(boss.keyRedeemLocation(), boss.id());
        HologramUtil.createBossHologram(boss.keyRedeemLocation().clone().toCenterLocation().add(0, 3, 0), boss);
        logger.info("Registered boss " + boss.id());
    }

    public void unregisterBoss(Boss boss) {
        bosses.remove(boss.id());
        keyLocToBossIDMap.remove(boss.keyRedeemLocation());
        HologramUtil.removeBossHologram(boss);
        logger.info("Unregistered boss " + boss.id());
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
            logger.info("Saving boss " + boss.id());
            storage.saveBoss(boss);
            return true;
        }).exceptionally(throwable -> {
            logger.severe("Failed to save boss " + boss.id());
            throwable.printStackTrace();
            return false;
        });
    }

    public CompletableFuture<Boss> load(String id) {
        Boss existing = boss(id);
        if (existing != null) {
            unregisterBoss(existing);
        }
        return CompletableFuture.supplyAsync(() -> storage.loadBoss(id)).thenApply(boss -> {
            if (boss != null) {
                registerBoss(boss);
            }
            return boss;
        }).exceptionally(throwable -> {
            logger.severe("Failed to load boss " + id);
            throwable.printStackTrace();
            return null;
        });
    }


    public CompletableFuture<Boolean> loadAll() {
        for (Boss boss : bosses.values()) {
            unregisterBoss(boss);
        }
        return CompletableFuture.supplyAsync(storage::loadAllBosses).thenApply((list) -> {
            list.forEach(this::registerBoss);
            logger.info("Loaded " + list.size() + " bosses");
            return true;
        }).exceptionally(throwable -> {
            logger.severe("Failed to load bosses");
            throwable.printStackTrace();
            return false;
        });

    }

}
