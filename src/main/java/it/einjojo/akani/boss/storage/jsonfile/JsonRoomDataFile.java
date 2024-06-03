package it.einjojo.akani.boss.storage.jsonfile;

import com.google.gson.Gson;
import it.einjojo.akani.boss.room.RoomData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public record JsonRoomDataFile(Gson gson, Path filePath) {
    public RoomData load() {
        if (!Files.exists(filePath)) return null;
        try (Reader reader = new FileReader(filePath.toFile())) {
            return gson.fromJson(reader, RoomData.class);
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            throw new RuntimeException(e);
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
