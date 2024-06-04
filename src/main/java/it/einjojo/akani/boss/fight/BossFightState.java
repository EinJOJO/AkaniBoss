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
     * Objective failed.
     */
    DEFEATED,
    /**
     * A fatal exception occurred where the fight cannot continue.
     */
    FATAL_EXCEPTION,
    /**
     * After the boss is defeated or player dies. The fight is over. No more actions can be taken.
     */
    CLOSED;


}
