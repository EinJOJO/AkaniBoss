package it.einjojo.akani.boss.listener;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.fight.BossFightState;
import it.einjojo.akani.boss.fight.state.defaults.DiscoveryStateLogic;
import it.einjojo.akani.boss.loot.Loot;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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
        if (bossFight.participants().isEmpty()) {
            bossFight.setState(BossFightState.DEFEATED);
        }
        diedPlayer.sendMessage(miniMessage().deserialize("<red>Du bist gestorben!"));
        bossFight.participantsPlayers().forEach(player -> player.sendMessage(miniMessage().deserialize("<red><player> ist gestorben!",
                Placeholder.parsed("player", diedPlayer.getName()))
        ));

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
        if (fight.isRunning()) {
            fight.setState(BossFightState.VICTORY);
        }
    }


    private MiniMessage miniMessage() {
        return MiniMessage.miniMessage();
    }

    @Override
    public void onParticipantJoin(BossFight fight, Player player) {
        fight.participantsPlayers().forEach(p -> p.sendMessage(miniMessage().deserialize("<green><player> ist dem Kampf beigetreten!",
                Placeholder.parsed("player", player.getName()))
        ));

    }

    @Override
    public void onParticipantLeave(BossFight fight, UUID uuid) {
        Player removedPlayer = Bukkit.getPlayer(uuid);
        if (removedPlayer != null) {
            if (fight.stateLogic() instanceof DiscoveryStateLogic discoveryStateLogic) {
                removedPlayer.hideBossBar(discoveryStateLogic.findBossRoomBossBar());
            }
        }

    }

    @Override
    public void onVictory(BossFight fight) {
        bossFightManager.closeBossFight(fight);
        List<Player> allParticipants = fight.allParticipantPlayers();
        for (Player player : allParticipants) {
            player.sendMessage(miniMessage().deserialize("<green>Der Boss wurde besiegt!"));
            player.sendMessage(miniMessage().deserialize("<yellow>Zeit: <green><time> Sekunden",
                    Placeholder.parsed("time", String.valueOf(Duration.ofMillis(System.currentTimeMillis() - fight.startedAt()).toSeconds()))
            ));
            player.sendMessage(miniMessage().deserialize("<yellow>Belohnungen :" + fight.boss().lootList().size()));
        }
        for (Loot loot : fight.boss().lootList()) {
            loot.grant(allParticipants);
        }

        spawnFireworks(fight.boss().keyRedeemLocation());
    }


    private void spawnFireworks(Location center) {
        Random random = new Random();
        AtomicInteger amount = new AtomicInteger(random.nextInt(6) + 3);
        Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
            if (amount.decrementAndGet() == 0) {
                task.cancel();
            }
            ;
            Location spawnLocation = center.clone();
            spawnLocation.add(random.nextInt(10) - 5, 0, random.nextInt(10) - 5);
            Firework firework = center.getWorld().spawn(spawnLocation, Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(random.nextInt(3) + 1);
            meta.addEffect(FireworkEffect.builder()
                    .withColor(Color.YELLOW)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .build());
            firework.setFireworkMeta(meta);

        }, 10, random.nextInt(25) + 10);
    }

    @Override
    public void onDefeat(BossFight fight) {
        fight.participantsPlayers().forEach(player -> player.sendMessage(miniMessage().deserialize("<red>Der Boss hat gewonnen!")));
        bossFightManager.closeBossFight(fight);
    }
}
