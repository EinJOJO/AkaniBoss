package it.einjojo.akani.boss.fight.state.defaults;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;
import it.einjojo.akani.boss.fight.state.StateLogic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;

public class IntroductionStateLogic implements StateLogic {
    private static final Duration INTRODUCTION_TIMEOUT = Duration.ofSeconds(30);
    private static final int DISTANCE_DIVIDER = 9;
    private final double maxDistanceSquaredUntilToFarAwayForPlayerToJoin;
    private final BossFight bossFight;
    private final long introductionEnd;
    private final Location spawnLocation;

    public IntroductionStateLogic(BossFight bossFight) {
        this.bossFight = bossFight;
        this.spawnLocation = bossFight.fightRoom().playerSpawnLocation();
        this.maxDistanceSquaredUntilToFarAwayForPlayerToJoin = spawnLocation.distanceSquared(bossFight.fightRoom().bossSpawnLocation()) / DISTANCE_DIVIDER;
        introductionEnd = System.currentTimeMillis() + INTRODUCTION_TIMEOUT.toMillis();
    }

    @Override
    public void tick() {
        if (introductionTimeIsOver() || anyParticipantToFarAwayFromEntrance()) {
            bossFight.setState(BossFightState.DISCOVERY);
            for (Player player : bossFight.participantsPlayers()) {
                player.sendMessage(Component.text("Die Tür ist nun verschlossen! Keine Unterstützung kann mehr nachkommen.", NamedTextColor.YELLOW));
                player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, 2, 0.2f);
            }
        }

    }

    private boolean introductionTimeIsOver() {
        return System.currentTimeMillis() > introductionEnd;
    }

    private boolean anyParticipantToFarAwayFromEntrance() {
        for (Player player : bossFight.participantsPlayers()) {
            if (!player.getWorld().equals(spawnLocation.getWorld())) continue;
            double distancePlayerToSpawnSquared = player.getLocation().distanceSquared(spawnLocation);
            if (distancePlayerToSpawnSquared > maxDistanceSquaredUntilToFarAwayForPlayerToJoin) {
                return true;
            }
        }
        return false;
    }


}
