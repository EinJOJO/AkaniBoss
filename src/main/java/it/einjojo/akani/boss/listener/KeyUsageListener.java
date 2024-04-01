package it.einjojo.akani.boss.listener;

import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.Boss;
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
    private final AkaniBoss bossInstance;

    public KeyUsageListener(JavaPlugin plugin, AkaniBoss bossInstance) {
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
        if (boss.checkKey(event.getItem())) {
            bossInstance.bossFightManager().unsealBoss(boss, event.getPlayer().getUniqueId());
            notifyBossKeyUsage(boss);
        } else {
            event.getPlayer().sendActionBar(Component.text("Der Schlüssel passt nicht zu diesem Boss.").color(NamedTextColor.RED));
        }

    }

    public void notifyBossKeyUsage(Boss boss) {
        for (Player player : boss.keyRedeemLocation().getNearbyPlayers(15)) {
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 1, 0.2f);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
            player.sendActionBar(
                    Component.text("Das Siegel von ").color(GREEN)
                            .append(boss.bossNameComponent())
                            .append(Component.text(" wurde gelöst!").color(GREEN)));

        }
    }
}
