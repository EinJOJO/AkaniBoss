package it.einjojo.akani.boss;

import it.einjojo.akani.boss.boss.BossManager;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.room.RoomManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniBoss {
    private final JavaPlugin plugin;
    private final BossManager bossManager;
    private final BossFightManager bossFightManager;
    private final RoomManager roomManager;

    public AkaniBoss(JavaPlugin plugin) {
        this.plugin = plugin;
        this.bossManager = new BossManager();
        this.bossFightManager = new BossFightManager();
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
}
