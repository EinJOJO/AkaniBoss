package it.einjojo.akani.boss.fight.state;

import it.einjojo.akani.boss.fight.BossFight;

public interface StateLogicFactory {

    StateLogic createLogic(BossFight bossFight);

}
