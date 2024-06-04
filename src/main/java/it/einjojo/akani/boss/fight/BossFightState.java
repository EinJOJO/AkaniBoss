package it.einjojo.akani.boss.fight;

public enum BossFightState {
    /**
     * When the world is being prepared for the fight
     */
    PREPARING,
    /**
     * When the time window is still open for new arrivals.
     */
    INTRODUCTION,
    /**
     * When they are walking towards the boss
     */
    DISCOVERY,
    /**
     * After first engagement with boss.
     */
    FIGHTING,
    /**
     * If bossfight was won by player
     */
    VICTORY,
    /**
     *
     */
    DEFEATED,
    /**
     * After the boss is defeated or player dies
     */
    CLOSED;


}
