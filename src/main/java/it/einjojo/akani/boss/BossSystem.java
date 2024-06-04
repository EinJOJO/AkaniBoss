package it.einjojo.akani.boss;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import it.einjojo.akani.boss.boss.BossManager;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.listener.FightListener;
import it.einjojo.akani.boss.listener.InputListener;
import it.einjojo.akani.boss.listener.KeyUsageListener;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import it.einjojo.akani.boss.room.RoomManager;
import it.einjojo.akani.boss.storage.jsonfile.GsonFactory;
import it.einjojo.akani.boss.storage.jsonfile.JsonFileBossStorage;
import it.einjojo.akani.boss.task.AsyncBossFightTickTask;
import it.einjojo.akani.boss.task.BossFightEntranceCheckTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BossSystem {
    private final @NotNull JavaPlugin plugin;
    private final @NotNull Gson gson;
    private final @NotNull BossManager bossManager;
    private final @NotNull BossFightManager bossFightManager;
    private final @NotNull RoomManager roomManager;

    public BossSystem(@NotNull JavaPlugin plugin) {
        Preconditions.checkNotNull(plugin);
        this.plugin = plugin;
        this.bossFightManager = new BossFightManager(this);
        this.gson = new GsonFactory(new RequirementFactory(bossFightManager)).createGson();
        this.bossManager = new BossManager(new JsonFileBossStorage(gson, plugin.getDataFolder().toPath().resolve("boss/")));
        this.roomManager = new RoomManager(plugin, this);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            load();
            registerListener();
            startTasks();
        });
    }

    public void load() {
        bossManager.loadAll();
        roomManager.load();
    }

    public void registerListener() {
        new InputListener(plugin());
        new KeyUsageListener(plugin(), this);
        new FightListener(bossFightManager(), plugin());
    }

    public void startTasks() {
        new BossFightEntranceCheckTask(this, plugin());
        new AsyncBossFightTickTask(bossFightManager(), plugin());
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


    public Gson gson() {
        return gson;
    }

}
