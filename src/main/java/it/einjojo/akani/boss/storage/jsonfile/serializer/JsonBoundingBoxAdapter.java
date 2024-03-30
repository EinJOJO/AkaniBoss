package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import org.bukkit.util.BoundingBox;

import java.lang.reflect.Type;

public class JsonBoundingBoxAdapter implements Adapter<BoundingBox> {
    @Override
    public JsonElement serialize(BoundingBox src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("minX", src.getMinX());
        object.addProperty("minY", src.getMinY());
        object.addProperty("minZ", src.getMinZ());
        object.addProperty("maxX", src.getMaxX());
        object.addProperty("maxY", src.getMaxY());
        object.addProperty("maxZ", src.getMaxZ());
        return object;
    }

    @Override
    public BoundingBox deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return new BoundingBox(
                object.get("minX").getAsDouble(),
                object.get("minY").getAsDouble(),
                object.get("minZ").getAsDouble(),
                object.get("maxX").getAsDouble(),
                object.get("maxY").getAsDouble(),
                object.get("maxZ").getAsDouble()
        );
    }


}
