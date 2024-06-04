package it.einjojo.akani.boss;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import it.einjojo.akani.boss.command.BossCommand;
import it.einjojo.akani.boss.command.FightCommand;
import it.einjojo.akani.boss.command.RoomCommand;
import it.einjojo.akani.boss.command.resolver.TemplateName;
import it.einjojo.akani.boss.command.resolver.TemplateNameResolver;
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
        registerCommands();
    }

    public void registerCommands() {
        liteCommands = LiteCommandsBukkit.builder("bossrooms", this)
                .commands(
                        new BossCommand(bossSystem),
                        new RoomCommand(bossSystem, this),
                        new FightCommand(bossSystem.bossFightManager())
                )
                .argument(TemplateName.class, new TemplateNameResolver(bossSystem.roomManager()))
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
