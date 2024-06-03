package it.einjojo.akani.boss.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightManager;
import org.bukkit.entity.Player;

@Command(name = "fight")
public class FightCommand {

    private final BossFightManager bossFightManager;

    public FightCommand(BossFightManager bossFightManager) {
        this.bossFightManager = bossFightManager;
    }

    @Execute(name = "invite")
    public void invite(@Context Player sender, @Arg("invite") Player target) {
        BossFight fight = bossFightManager.playerBossFight(target.getUniqueId());
        if (fight != null) {
            target.sendMessage("/fight join " + sender.getName());
            return;
        }
        sender.sendMessage("You don't have a boss fight");
    }

    @Execute(name = "join")
    public void accept(@Context Player sender, @Arg("accept") Player target) {
        BossFight fight = bossFightManager.playerBossFight(target.getUniqueId());
        if (fight != null) {
            fight.addParticipant(sender);
            return;
        }
        sender.sendMessage("Does not have bossfight");
    }

}
