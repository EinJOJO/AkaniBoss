package it.einjojo.akani.boss.storage.jsonfile;

import com.google.gson.Gson;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.storage.BossStorage;
import it.einjojo.akani.boss.storage.StorageException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A folder storage for bosses using json files
 *
 * @param gson       The gson instance
 * @param bossFolder The folder to store the bosses
 */
public record JsonFileBossStorage(Gson gson, Path bossFolder) implements BossStorage {

    @Override
    public void saveBoss(Boss boss) throws StorageException {
        try {
            Files.createDirectories(bossFolder);
            Path file = bossFolder.resolve(boss.id() + ".json");
            try (Writer writer = Files.newBufferedWriter(file)) {
                gson.toJson(boss, writer);
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }

    }


    @Override
    public Boss loadBoss(String id) throws StorageException {
        return loadBossByPath(bossFolder.resolve(id + ".json"));
    }

    public Boss loadBossByPath(Path path) throws StorageException {
        if (!Files.exists(path)) return null;
        try (Reader reader = Files.newBufferedReader(path)) {
            return gson.fromJson(reader, Boss.class);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Boss> loadAllBosses() throws StorageException {
        List<Boss> result = new ArrayList<>();
        try {
            Files.createDirectories(bossFolder);
            try (Stream<Path> paths = Files.list(bossFolder)) {
                paths.filter(path -> path.toString().endsWith(".json"))
                        .forEach(path -> {
                            try {
                                Boss boss = loadBossByPath(path);
                                if (boss != null) {
                                    result.add(boss);
                                }
                            } catch (StorageException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } catch (IOException | RuntimeException e) {
            throw new StorageException(e);
        }
        return result;
    }
}
