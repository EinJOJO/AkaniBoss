package it.einjojo.akani.boss.storage;

import it.einjojo.akani.boss.boss.Boss;

import java.util.List;

public interface BossStorage {
    void saveBoss(Boss boss);

    Boss loadBoss(String id);

    List<Boss> loadAllBosses();

}
