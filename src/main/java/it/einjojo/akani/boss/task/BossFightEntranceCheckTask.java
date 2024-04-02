package it.einjojo.akani.boss.task;

import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.Boss;
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

public class BossFightEntranceCheckTask implements Runnable {

    private final AkaniBoss akaniBoss;

    public BossFightEntranceCheckTask(AkaniBoss akaniBoss) {
        this.akaniBoss = akaniBoss;
    }

    public void start(JavaPlugin plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 10);
    }

    @Override
    public void run() {
        Collection<Boss> bossAltars = akaniBoss.bossManager().bosses().values();
        for (Boss boss : bossAltars) {
            if (!boss.keyRedeemLocation().getChunk().isLoaded()) return;
            for (Entity inBoundary : boss.keyRedeemLocation().getWorld().getNearbyEntities(boss.dungeonEntrance())) {
                if (!(inBoundary instanceof Player player)) continue;
                Requirement failed = boss.testRequirements(player);
                if (failed != null) {
                    kickOut(boss, player, failed);
                    return;
                }
                participate(player, boss);
            }
        }
    }

    public void participate(Player player, Boss boss) {
        akaniBoss.bossFightManager().participateInFight(player.getUniqueId(), boss);
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
