package it.einjojo.akani.boss.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.command.resolver.TemplateName;
import it.einjojo.akani.boss.input.RoomCreator;
import it.einjojo.akani.boss.room.RoomManager;
import it.einjojo.akani.boss.room.RoomTemplate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Command(name = "bossroom")
public class RoomCommand {

    private final BossSystem system;
    private final JavaPlugin plugin;

    public RoomCommand(BossSystem system, JavaPlugin plugin) {
        this.system = system;
        this.plugin = plugin;
    }

    @Execute(name = "info")
    public void info(@Context CommandSender sender) {
        sender.sendMessage("Bossroom Info");

    }

    @Execute(name = "setup")
    public void setup(@Context Player player, @Arg("templatename") TemplateName templateName) {
        player.sendMessage("Setup room with template " + templateName);
        RoomManager roomManager = system.roomManager();
        RoomTemplate roomTemplate = roomManager.roomTemplate(templateName.name());
        if (roomTemplate == null) {
            player.sendMessage("Room template not found");
            return;
        }
        new RoomCreator(roomManager, plugin, player, roomTemplate, () -> {
            player.sendMessage("Room created");
        }).startSetup();

    }
}
