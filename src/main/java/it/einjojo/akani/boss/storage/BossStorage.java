package it.einjojo.akani.boss.storage;

import it.einjojo.akani.boss.boss.Boss;

import java.util.List;

/**
 * Interface for storing and loading bosses
 */
public interface BossStorage {
    void saveBoss(Boss boss) throws StorageException;

    Boss loadBoss(String id) throws StorageException;

    List<Boss> loadAllBosses() throws StorageException;


}
