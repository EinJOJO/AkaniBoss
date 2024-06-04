package it.einjojo.akani.boss.fight.state.defaults;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.state.StateLogic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class FatalExceptionStateLogic implements StateLogic {
    private final BossFight bossFight;
    private final List<Player> needsReset = new LinkedList<>();
    private int resetTimer = 10;

    public FatalExceptionStateLogic(BossFight bossFight) {

        this.bossFight = bossFight;
        report();
    }


    public void report() {
        Title title = Title.title(
                Component.text("Ein Fataler Fehler ist aufgetreten", NamedTextColor.RED),
                Component.text("Leider kann der Bosskampf nicht fortgesetzt werden...", NamedTextColor.RED)
        );
        for (Player player : bossFight.participantsPlayers()) {
            player.showTitle(title);
            player.setWalkSpeed(0.0f);
            player.setInvulnerable(true);
            needsReset.add(player);
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_DEATH, 0.4f, 0.2f);
        }
    }

    @Override
    public void tick() {
        resetTimer--;
        if (resetTimer <= 0) {
            bossFight.bossFightManager().closeBossFight(bossFight);
        }
    }

    @Override
    public void disable() {
        for (Player player : needsReset) {
            player.setWalkSpeed(0.2f);
            player.setInvulnerable(false);
        }
    }
}
