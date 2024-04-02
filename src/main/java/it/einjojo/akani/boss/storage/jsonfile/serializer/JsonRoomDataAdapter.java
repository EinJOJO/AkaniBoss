package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import it.einjojo.akani.boss.room.RoomData;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class JsonRoomDataAdapter implements Adapter<RoomData> {
    @Override
    public RoomData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        Location playerSpawn = context.deserialize(object.get("player-spawn"), Location.class);
        Location bossSpawn = context.deserialize(object.get("boss-spawn"), Location.class);
        return new RoomData(playerSpawn, bossSpawn);
    }

    @Override
    public JsonElement serialize(RoomData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("player-spawn", context.serialize(src.playerSpawnLocation()));
        object.add("boss-spawn", context.serialize(src.bossSpawnLocation()));
        return object;
    }
}
