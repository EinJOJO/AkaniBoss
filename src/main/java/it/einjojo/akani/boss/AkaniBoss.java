package it.einjojo.akani.boss;

import com.google.gson.Gson;
import it.einjojo.akani.boss.boss.BossManager;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import it.einjojo.akani.boss.room.RoomManager;
import it.einjojo.akani.boss.storage.BossStorage;
import it.einjojo.akani.boss.storage.jsonfile.GsonFactory;
import it.einjojo.akani.boss.storage.jsonfile.JsonFileBossStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniBoss {
    private final JavaPlugin plugin;
    private final Gson gson;
    private final BossStorage bossStorage;
    private final RequirementFactory requirementFactory;
    private final BossManager bossManager;
    private final BossFightManager bossFightManager;
    private final RoomManager roomManager;

    public AkaniBoss(JavaPlugin plugin) {
        this.plugin = plugin;
        this.bossFightManager = new BossFightManager(this);
        this.requirementFactory = new RequirementFactory(bossFightManager);
        gson = new GsonFactory(requirementFactory).createGson();
        bossStorage = new JsonFileBossStorage(gson, plugin.getDataFolder().toPath().resolve("boss/"));
        this.bossManager = new BossManager(bossStorage);
        this.roomManager = new RoomManager(plugin);
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    public BossManager bossManager() {
        return bossManager;
    }

    public BossFightManager bossFightManager() {
        return bossFightManager;
    }

    public RoomManager roomManager() {
        return roomManager;
    }

    public RequirementFactory requirementFactory() {
        return requirementFactory;
    }

    public Gson gson() {
        return gson;
    }

    public BossStorage bossStorage() {
        return bossStorage;
    }
}
