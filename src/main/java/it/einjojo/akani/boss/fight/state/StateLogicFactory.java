package it.einjojo.akani.boss.fight.state;

import it.einjojo.akani.boss.fight.BossFight;

public class StateLogicFactory {

    public StateLogic createLogic(BossFight bossFight) {
        return switch (bossFight.state()) {
            case PREPARING -> new PrepareGenerateWorld(bossFight);
            default -> throw new IllegalStateException("Unknown state: " + bossFight.state());
        };
    }

}
