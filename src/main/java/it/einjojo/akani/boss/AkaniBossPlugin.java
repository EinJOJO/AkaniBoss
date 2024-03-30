package it.einjojo.akani.boss;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import it.einjojo.akani.boss.command.BossCommand;
import it.einjojo.akani.boss.listener.InputListener;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniBossPlugin extends JavaPlugin {
    private AkaniBoss akaniBoss;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        akaniBoss = new AkaniBoss(this);
        akaniBoss.roomManager().load();
        liteCommands = LiteCommandsBukkit.builder("bossrooms", this)
                .commands(
                        new BossCommand(akaniBoss)
                )
                .build();
        new InputListener();
    }

    @Override
    public void onDisable() {
        akaniBoss = null;
    }

    public AkaniBoss akaniBoss() {
        return akaniBoss;
    }
}
