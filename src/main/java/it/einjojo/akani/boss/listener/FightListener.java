package it.einjojo.akani.boss.listener;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * Event Based Logic for boss fights.
 * e.g. Commands, Death, Quit, etc.
 */
public class FightListener implements Listener {

    private final BossFightManager bossFightManager;
    private final JavaPlugin plugin;

    public FightListener(BossFightManager bossFightManager, JavaPlugin plugin) {
        this.bossFightManager = bossFightManager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        BossFight bossFight = bossFightManager.playerBossFight(event.getPlayer().getUniqueId());
        if (bossFight == null) return;
        closeFightIfOnlyOneParticipantOrForEachParticipant(bossFight, (player) -> {
            player.sendMessage(miniMessage().deserialize("<red><player> <gray>hat den Bosskampf verlassen",
                    Placeholder.parsed("player", event.getPlayer().getName()))
            );
        });
        bossFight.removeParticipant(event.getPlayer().getUniqueId());
    }

    private void closeFightIfOnlyOneParticipantOrForEachParticipant(BossFight bossFight, Consumer<Player> playerConsumer) {
        if (bossFight.participants().size() <= 1) {
            bossFightManager.closeBossFight(bossFight);
            return;
        }
        for (Player player : bossFight.participantsPlayers()) {
            playerConsumer.accept(player);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        BossFight bossFight = bossFightManager.playerBossFight(event.getEntity().getUniqueId());
        if (bossFight == null) return;
        Player diedPlayer = event.getPlayer();
        closeFightIfOnlyOneParticipantOrForEachParticipant(bossFight, (player) -> {
            player.sendMessage(miniMessage().deserialize("<red><player> ist gestorben!",
                    Placeholder.parsed("player", diedPlayer.getName()))
            );
        });
        diedPlayer.sendMessage(miniMessage().deserialize("<red>Du bist gestorben!"));
        event.deathMessage(null);
        Bukkit.getScheduler().runTask(plugin, () -> {
            diedPlayer.spigot().respawn();
            Location loc = bossFight.boss().keyRedeemLocation().clone();
            loc.setDirection(diedPlayer.getLocation().getDirection());
            diedPlayer.teleportAsync(loc);
        });


    }

    private MiniMessage miniMessage() {
        return MiniMessage.miniMessage();
    }
}
