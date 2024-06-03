package it.einjojo.akani.boss.fight.state;

import it.einjojo.akani.boss.fight.BossFight;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public record PrepareGenerateWorld(BossFight bossFight) implements StateLogic {

    @Override
    public void tick() {
        for (Player player : bossFight().participantsPlayers()) {
            player.sendActionBar(MiniMessage.miniMessage().deserialize("<gray><i>Die Welt wird errichtet..."));
        }

    }

    @Override
    public void onParticipantJoin(Player player) {

    }
}
