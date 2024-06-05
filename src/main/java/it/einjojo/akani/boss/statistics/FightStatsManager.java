package it.einjojo.akani.boss.statistics;

import java.util.UUID;

public class FightStatsManager {


    public FightStats createFightStats(UUID player) {
        return new FightStats(player);
    }

}
