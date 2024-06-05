package it.einjojo.akani.boss.storage;

import it.einjojo.akani.boss.boss.Boss;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for storing and loading bosses
 */
public interface BossStorage {
    void saveBoss(Boss boss) throws StorageException;

    @Nullable
    Boss loadBoss(String id) throws StorageException;

    List<Boss> loadAllBosses() throws StorageException;


}
