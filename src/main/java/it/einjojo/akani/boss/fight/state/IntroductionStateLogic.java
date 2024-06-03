package it.einjojo.akani.boss.fight.state;

import it.einjojo.akani.boss.BossSystemPlugin;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.UUID;

public class IntroductionStateLogic implements StateLogic {
    private static final Duration INTRODUCTION_TIMEOUT = Duration.ofSeconds(10);
    private final BossFight bossFight;
    private final long introductionEnd = System.currentTimeMillis() + INTRODUCTION_TIMEOUT.toMillis();
    private final Location spawnLocation;

    public IntroductionStateLogic(BossFight bossFight) {
        this.bossFight = bossFight;
        spawnLocation = bossFight.fightRoom().world().getSpawnLocation();
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() > introductionEnd) {
            stateToDiscovery();
        }
    }

    public void stateToDiscovery() {
        bossFight.setState(BossFightState.DISCOVERY);
        for (Player player : bossFight.participantsPlayers()) {
            player.sendMessage(Component.text("Die Tür ist verschlossen! Keine Unterstützung kann mehr nachkommen.", NamedTextColor.YELLOW));
        }
    }

    @Override
    public void onParticipantJoin(Player player) {
        JavaPlugin plugin = JavaPlugin.getPlugin(BossSystemPlugin.class);
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (bossFight.participants().isEmpty()) {
                player.teleportAsync(spawnLocation);
            } else {
                UUID target = null;
                for (UUID uuid : bossFight.participants()) {
                    target = uuid;
                    if (!target.equals(player.getUniqueId())) {
                        break;
                    }
                }
                if (target == null) {
                    player.teleportAsync(spawnLocation);
                    return;
                }

                Player targetPlayer = Bukkit.getPlayer(target);
                if (targetPlayer == null) {
                    player.teleportAsync(spawnLocation);
                    return;
                }
                ;
                player.teleportAsync(targetPlayer.getLocation());
            }
        });
    }

}
