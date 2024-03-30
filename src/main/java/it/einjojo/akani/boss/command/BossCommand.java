package it.einjojo.akani.boss.command;


import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.input.BossCreator;
import org.bukkit.entity.Player;

@Command(name = "boss")
public class BossCommand {
    private final AkaniBoss akaniBoss;

    public BossCommand(AkaniBoss akaniBoss) {
        this.akaniBoss = akaniBoss;
    }

    @Execute(name = "setup")
    public void createBoss(@Context Player executor) {
        new BossCreator(akaniBoss, executor).askForInput();
    }

}
