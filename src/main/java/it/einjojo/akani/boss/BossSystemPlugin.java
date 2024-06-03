package it.einjojo.akani.boss;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import it.einjojo.akani.boss.command.BossCommand;
import it.einjojo.akani.boss.command.RoomCommand;
import it.einjojo.akani.boss.listener.InputListener;
import it.einjojo.akani.boss.listener.KeyUsageListener;
import it.einjojo.akani.boss.task.AsyncBossFightTickTask;
import it.einjojo.akani.boss.task.BossFightEntranceCheckTask;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BossSystemPlugin extends JavaPlugin {
    private BossSystem bossSystem;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        bossSystem = new BossSystem(this);
        bossSystem.roomManager().cleanupWorldContainer().thenRun(() -> {
            getLogger().info("World container cleaned up async.");
        });
        bossSystem.roomManager().load();
        bossSystem.bossManager().loadAll();
        new BossFightEntranceCheckTask(bossSystem, this);
        new AsyncBossFightTickTask(bossSystem.bossFightManager(), this);
        new InputListener(this);
        new KeyUsageListener(this, bossSystem);
        registerCommands();
    }

    public void registerCommands() {
        liteCommands = LiteCommandsBukkit.builder("bossrooms", this)
                .commands(
                        new BossCommand(bossSystem),
                        new RoomCommand(bossSystem, this)
                )
                .build();
    }

    @Override
    public void onDisable() {
        bossSystem = null;
        liteCommands.unregister();
        liteCommands = null;

    }

    public BossSystem akaniBoss() {
        return bossSystem;
    }
}
