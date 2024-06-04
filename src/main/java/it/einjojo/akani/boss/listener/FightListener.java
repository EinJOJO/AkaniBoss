package it.einjojo.akani.boss.listener;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.fight.BossFightState;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Event Based Logic for boss fights.
 * e.g. Commands, Death, Quit, etc.
 */
public class FightListener implements Listener, BossFight.Listener {
    private final BossFightManager bossFightManager;
    private final JavaPlugin plugin;

    public FightListener(BossFightManager bossFightManager, JavaPlugin plugin) {
        this.bossFightManager = bossFightManager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        bossFightManager.setDefaultChangeListener(this);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        BossFight bossFight = bossFightManager.playerBossFight(event.getPlayer().getUniqueId());
        if (bossFight == null) return;
        bossFight.removeParticipant(event.getPlayer().getUniqueId());
        if (bossFight.participants().isEmpty()) {
            bossFight.setState(BossFightState.DEFEATED);
        } else {
            for (Player player : bossFight.participantsPlayers()) {
                player.sendMessage(miniMessage().deserialize("<red><player> hat den Kampf verlassen!",
                        Placeholder.parsed("player", event.getPlayer().getName()))
                );
            }
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        BossFight bossFight = bossFightManager.playerBossFight(event.getEntity().getUniqueId());
        if (bossFight == null) return;
        Player diedPlayer = event.getPlayer();
        bossFight.removeParticipant(diedPlayer.getUniqueId());
        diedPlayer.sendMessage(miniMessage().deserialize("<red>Du bist gestorben!"));
        event.deathMessage(null);
        Bukkit.getScheduler().runTask(plugin, () -> {
            diedPlayer.spigot().respawn();
            Location loc = bossFight.boss().keyRedeemLocation().clone();
            loc.setDirection(diedPlayer.getLocation().getDirection());
            diedPlayer.teleportAsync(loc);
        });
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        BossFight fight = bossFightManager.bossMobRegistry().getByMobId(event.getEntity().getUniqueId());
        if (fight == null) return;
        fight.setState(BossFightState.VICTORY);

    }

    private MiniMessage miniMessage() {
        return MiniMessage.miniMessage();
    }

    @Override
    public void onParticipantJoin(BossFight fight, Player player) {

    }

    @Override
    public void onParticipantLeave(BossFight fight, UUID uuid) {

    }

    @Override
    public void onVictory(BossFight fight) {

    }

    @Override
    public void onDefeat(BossFight fight) {

    }
}
