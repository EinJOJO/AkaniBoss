package it.einjojo.akani.boss.fight.state;

import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossFightState;

public interface StateLogic {
    void update(BossFight currentFight);
}
