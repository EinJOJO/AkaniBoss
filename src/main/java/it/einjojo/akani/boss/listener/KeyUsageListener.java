package it.einjojo.akani.boss.listener;

import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static org.bukkit.event.Event.Result.DENY;

public class KeyUsageListener implements Listener {
    private final BossSystem bossInstance;

    public KeyUsageListener(JavaPlugin plugin, BossSystem bossInstance) {
        this.bossInstance = bossInstance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;
        Boss boss = bossInstance.bossManager().bossByLocation(event.getClickedBlock().getLocation());
        if (boss == null) return;
        event.setCancelled(true);
        event.setUseInteractedBlock(DENY);
        event.setUseItemInHand(DENY);
        Requirement failed = boss.testKeyRedeemRequirements(event.getPlayer());
        if (failed != null) {
            failedRequirement(failed, event.getPlayer(), boss);
            return;
        }
        if (boss.checkKey(event.getItem())) {
            bossInstance.bossFightManager().allowEntrance(event.getPlayer().getUniqueId(), boss);
            notifyBossKeyUsage(event.getPlayer(), boss);
        } else {
            event.getPlayer().sendActionBar(Component.text("Der Schlüssel passt nicht zu diesem Boss.").color(NamedTextColor.RED));
        }

    }

    public void failedRequirement(Requirement failedRequirement, Player player, Boss boss) {
        player.sendActionBar(failedRequirement.denyMessage(player));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
    }

    public void notifyBossKeyUsage(Player player, Boss boss) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        player.sendActionBar(Component.text("Der Schlüssel passt!").color(GREEN));
    }
}
