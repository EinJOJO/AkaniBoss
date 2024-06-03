package it.einjojo.akani.boss.fight.state.defaults;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;
import it.einjojo.akani.boss.fight.state.StateLogic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.time.Duration;

public class IntroductionStateLogic implements StateLogic {
    private static final Duration INTRODUCTION_TIMEOUT = Duration.ofSeconds(30);
    private final BossFight bossFight;
    private final long introductionEnd = System.currentTimeMillis() + INTRODUCTION_TIMEOUT.toMillis();

    public IntroductionStateLogic(BossFight bossFight) {
        this.bossFight = bossFight;
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


}
