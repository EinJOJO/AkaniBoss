package it.einjojo.akani.boss.fight.state;

import org.bukkit.entity.Player;

public interface StateLogic {
    void tick();

    void onParticipantJoin(Player player);

}
