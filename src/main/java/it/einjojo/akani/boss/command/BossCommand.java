package it.einjojo.akani.boss.command;


import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.boss.BossBuilder;
import it.einjojo.akani.boss.boss.BossDifficulty;
import it.einjojo.akani.boss.input.BossCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Command(name = "boss")
public class BossCommand {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final Component BOSS_NOT_FOUND = miniMessage.deserialize("<red>Der Boss konnte nicht gefunden werden!");
    private final AkaniBoss akaniBoss;

    public BossCommand(AkaniBoss akaniBoss) {
        this.akaniBoss = akaniBoss;
    }

    @Execute(name = "setup")
    public void createBoss(@Context Player executor) {
        new BossCreator(akaniBoss, executor).askForInput();
    }

    @Execute(name = "info")
    public void bossInfo(@Context CommandSender executor) {
        Set<String> bossIds = akaniBoss.bossManager().bosses().keySet();
        executor.sendMessage(miniMessage.deserialize("Boss Info: " + bossIds.size()));
        for (String bossID : akaniBoss.bossManager().bosses().keySet()) {
            executor.sendMessage(miniMessage.deserialize("<click:suggest_command:'/boss info %s'><hover:show_text:'Klicke um mehr zu erfahren.'><gray>- <green>%s</hover></click>".formatted(bossID, bossID)));
        }
    }

    @Execute(name = "info")
    public void bossInfo(@Context CommandSender executor, @Arg("boss") String bossId) {
        Boss boss = akaniBoss.bossManager().boss(bossId);
        if (boss == null) {
            executor.sendMessage(BOSS_NOT_FOUND);
            return;
        }
        executor.sendMessage(boss.debugInfoComponent());
    }

    @Execute(name = "reload")
    public void reload(@Context CommandSender executor, @Arg("boss") Optional<String> optionalBossId) {
        optionalBossId.ifPresentOrElse((bossId) -> {
            akaniBoss.bossManager().load(bossId);
            executor.sendMessage(miniMessage.deserialize("<green>Der Boss wurde neu geladen!"));
        }, () -> {
            akaniBoss.bossManager().loadAll();
            executor.sendMessage(miniMessage.deserialize("<green>Alle Bosse wurden neu geladen!"));
        });

    }

    @Execute(name = "test")
    public void test(@Context CommandSender executor) {
        Boss boss = new BossBuilder()
                .id("test")
                .name("Test Boss")
                .difficulty(BossDifficulty.NORMAL)
                .level(1)
                .keyItem(new ItemStack(Material.DIAMOND))
                .entranceBox(new BoundingBox(0f, 0f, 0f, 10f, 10f, 10f))
                .keyRedeemLocation(new Location(Bukkit.getWorld("world"), 0, 0, 0))
                .requirements(List.of())
                .roomTemplateName("world")
                .build();
        akaniBoss.bossStorage().saveBoss(boss);
    }

}
