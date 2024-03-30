package it.einjojo.akani.boss.storage.jsonfile;

import com.google.gson.Gson;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.storage.BossStorage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonFileBossStorage implements BossStorage {
    private final Gson gson;
    private final Path bossFolder;

    public JsonFileBossStorage(Gson gson, Path folder) {
        this.gson = gson;
        this.bossFolder = folder;
    }

    @Override
    public void saveBoss(Boss boss) {
        try {
            Files.createDirectories(bossFolder);
            Path file = bossFolder.resolve(boss.id() + ".json");
            try (FileWriter writer = new FileWriter(file.toFile())) {
                gson.toJson(boss, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Boss loadBoss(String id) {
        return loadBossByPath(bossFolder.resolve(id + ".json"));
    }

    public Boss loadBossByPath(Path path) {
        if (!Files.exists(path)) return null;
        try {
            return gson.fromJson(Files.newBufferedReader(path), Boss.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Boss> loadAllBosses() {
        try {
            Files.createDirectories(bossFolder);
            try (Stream<Path> stream = Files.list(bossFolder)) {
                return stream.filter(path -> path.toString().endsWith(".json"))
                        .map(this::loadBossByPath).filter(Objects::nonNull).collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
