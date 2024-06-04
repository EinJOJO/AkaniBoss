package it.einjojo.akani.boss.task;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightManager;
import org.bukkit.plugin.java.JavaPlugin;

public record AsyncBossFightTickTask(BossFightManager bossFightManager, JavaPlugin plugin) implements Runnable {
    public AsyncBossFightTickTask {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 5);
    }
    @Override
    public void run() {
        for (BossFight fight : bossFightManager.activeBossFights()) {
            fight.tick();
        }
    }
}
