package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import it.einjojo.akani.boss.boss.mob.BossMob;

import java.lang.reflect.Type;

public class JsonBossMobAdapter implements Adapter<BossMob<?>>{
    @Override
    public BossMob<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(BossMob src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
