package it.einjojo.akani.boss.storage.jsonfile;

import com.google.gson.Gson;
import it.einjojo.akani.boss.room.RoomData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public record JsonRoomDataFile(Gson gson, Path filePath) {
    public RoomData load() {
        if (!Files.exists(filePath)) return null;
        try {
            return gson.fromJson(new FileReader(filePath.toFile()), RoomData.class);
        } catch (FileNotFoundException ignore) {
        }
        return null;
    }

    public void save(RoomData roomData) {
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            gson.toJson(roomData, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
