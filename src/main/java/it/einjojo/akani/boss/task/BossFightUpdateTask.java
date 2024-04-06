package it.einjojo.akani.boss.task;

import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.fight.BossFight;
import org.bukkit.plugin.java.JavaPlugin;

public class BossFightUpdateTask implements Runnable {

    private final BossSystem boss;

    public BossFightUpdateTask(BossSystem boss) {
        this.boss = boss;
    }

    public void start(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 10);
    }

    @Override
    public void run() {
        for (BossFight fight : boss.bossFightManager().activeBossFights()) {
            fight.update();
        }
    }
}
