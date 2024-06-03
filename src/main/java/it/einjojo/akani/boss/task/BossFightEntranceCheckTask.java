package it.einjojo.akani.boss.task;

import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;

public record BossFightEntranceCheckTask(BossSystem bossSystem, JavaPlugin plugin) implements Runnable {

    public BossFightEntranceCheckTask {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 10);
    }

    @Override
    public void run() {
        Collection<Boss> bossAltars = bossSystem.bossManager().bosses().values();
        for (Boss boss : bossAltars) {
            if (!boss.keyRedeemLocation().getChunk().isLoaded()) continue;
            for (Entity inBoundary : boss.keyRedeemLocation().getWorld().getNearbyEntities(boss.dungeonEntrance())) {
                if (!(inBoundary instanceof Player player)) continue;
                Requirement failed = boss.testEntranceRequirements(player);
                if (failed != null) {
                    kickOut(boss, player, failed);
                    continue;
                }
                participate(player, boss);
            }
        }
    }

    public void participate(Player player, Boss boss) {
        BossFight existing = bossSystem.bossFightManager().playerBossFight(player.getUniqueId());
        if (existing != null) return;
        BossFight newBossFight = bossSystem.bossFightManager().startNewBossFight(boss);
        newBossFight.addParticipant(player);
        bossSystem.bossFightManager().clearEntrance(player.getUniqueId(), boss);
    }


    public void kickOut(Boss boss, Player player, Requirement failedRequirement) {
        Vector entityVector = player.getLocation().toVector();
        Vector velocity = boss.keyRedeemLocation().toVector().subtract(entityVector).normalize().add(new Vector(0, 0.5f, 0)).multiply(1.5f);
        player.setVelocity(velocity);
        player.showTitle(noAccessTitle(player, failedRequirement));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    public Title noAccessTitle(Player player, Requirement failedRequirement) {
        return Title.title(Component.text("Kein Zugang!").color(NamedTextColor.RED).decorate(TextDecoration.BOLD), failedRequirement.denyMessage(player));
    }
}
