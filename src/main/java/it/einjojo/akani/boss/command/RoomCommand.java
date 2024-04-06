package it.einjojo.akani.boss.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import it.einjojo.akani.boss.BossSystem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "bossroom")
public class RoomCommand {

    private final BossSystem system;

    public RoomCommand(BossSystem system) {
        this.system = system;
    }

    @Execute(name = "info")
    public void info(@Context CommandSender sender) {
        sender.sendMessage("Bossroom Info");

    }

    @Execute(name = "setup")
    public void setup(@Context Player player, @Arg("templatename") String templateName) {
        player.sendMessage("Setup room with template " + templateName);
        system.roomManager().createRoomByTemplate( templateName).thenAccept((room) -> {
            player.teleportAsync(room.world().getSpawnLocation());
            player.sendMessage("Room setup complete");
        }).exceptionally(throwable -> {
            player.sendMessage("Failed to setup room: " + throwable.getMessage());
            return null;
        });

    }
}
