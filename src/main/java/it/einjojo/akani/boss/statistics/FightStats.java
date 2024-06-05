package it.einjojo.akani.boss.statistics;

import java.util.UUID;

/**
 * Represents the statistics of a boss-fight.
 */
public class FightStats {
    private final UUID player;
    private int damageDealt;
    private int damageTaken;
    private int mobKills;
    private boolean died;
    private boolean killedBoss;

    public FightStats(UUID player) {
        this.player = player;
    }
}
