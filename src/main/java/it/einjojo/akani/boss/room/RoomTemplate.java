package it.einjojo.akani.boss.room;

import it.einjojo.akani.boss.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @param templateName        the name of the template
 * @param worldTemplateFolder the folder of the world template
 * @param roomData            the data of the room
 */
public record RoomTemplate(String templateName, Path worldTemplateFolder, RoomData roomData) {
    public void cleanFolder() throws IOException {
        Files.deleteIfExists(worldTemplateFolder.resolve("uid.dat"));
        Files.deleteIfExists(worldTemplateFolder.resolve("session.lock"));
        FileUtil.deleteFolder(worldTemplateFolder.resolve("playerdata"));
    }

    /**
     * @return a new RoomTemplate with the given RoomData
     */
    public RoomTemplate setRoomData(RoomData roomData) {
        return new RoomTemplate(templateName, worldTemplateFolder, roomData);
    }

    public Path roomDataPath() {
        return worldTemplateFolder.resolve("roomdata.json");
    }

}
