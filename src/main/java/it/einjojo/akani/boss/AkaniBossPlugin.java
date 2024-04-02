package it.einjojo.akani.boss;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import it.einjojo.akani.boss.command.BossCommand;
import it.einjojo.akani.boss.listener.InputListener;
import it.einjojo.akani.boss.listener.KeyUsageListener;
import it.einjojo.akani.boss.task.BossFightEntranceCheckTask;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniBossPlugin extends JavaPlugin {
    private AkaniBoss akaniBoss;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        akaniBoss = new AkaniBoss(this);
        akaniBoss.roomManager().cleanupWorldContainer().thenRun(() -> {
            getLogger().info("World container cleaned up async.");
        });
        akaniBoss.roomManager().load();
        akaniBoss.bossManager().loadAll();
        new BossFightEntranceCheckTask(akaniBoss).start(this);
        new InputListener(this);
        new KeyUsageListener(this, akaniBoss);
        registerCommands();
    }

    public void registerCommands() {
        liteCommands = LiteCommandsBukkit.builder("bossrooms", this)
                .commands(
                        new BossCommand(akaniBoss)
                )
                .build();
    }

    @Override
    public void onDisable() {
        akaniBoss = null;
        liteCommands.unregister();
        liteCommands = null;

    }

    public AkaniBoss akaniBoss() {
        return akaniBoss;
    }
}
