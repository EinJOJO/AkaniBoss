package it.einjojo.akani.boss.fight;

import it.einjojo.akani.boss.fight.state.NotifyAboutWorldPreparation;
import it.einjojo.akani.boss.fight.state.StateLogic;

import java.util.List;

public enum BossFightState implements StateLogic {
    /**
     * When the world is being prepared for the fight
     */
    PREPARING(List.of(new NotifyAboutWorldPreparation())),
    /**
     * When the time window is still open for new arrivals.
     */
    INTRODUCTION(List.of()),
    /**
     * When they are walking towards the boss
     */
    DISCOVERY,
    /**
     * After first engagement with boss.
     */
    FIGHTING,
    /**
     * After the boss is defeated
     */
    ENDING;
    private final List<StateLogic> logic;

    BossFightState() {
        this.logic = List.of();
    }

    BossFightState(List<StateLogic> logic) {
        this.logic = logic;
    }

    public void update(BossFight currentFight) {
        for (StateLogic stateLogic : logic) {
            stateLogic.update(currentFight);
        }
    }
}
