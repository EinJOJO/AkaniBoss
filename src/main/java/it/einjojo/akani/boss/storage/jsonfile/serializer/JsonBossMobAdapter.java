package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import it.einjojo.akani.boss.boss.mob.BossMob;
import it.einjojo.akani.boss.boss.mob.MythicBossMobFactory;
import it.einjojo.akani.boss.boss.mob.VanillaMobFactory;

import java.lang.reflect.Type;

public class JsonBossMobAdapter implements Adapter<BossMob<?>> {
    @Override
    public BossMob<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String mobName = json.getAsJsonObject().get("name").getAsString();
        String factoryName = json.getAsJsonObject().get("factory").getAsString();
        if (factoryName.equals(VanillaMobFactory.class.getSimpleName())) {
            return new VanillaMobFactory().createBossMob(mobName);
        } else if (factoryName.equals(MythicBossMobFactory.class.getSimpleName())) {
            return new MythicBossMobFactory().createBossMob(mobName);
        } else {
            throw new IllegalArgumentException("Factory not found: " + factoryName);
        }
    }

    @Override
    public JsonElement serialize(BossMob src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("name", src.mobName());
        object.addProperty("factory", src.factory().getClass().getSimpleName());
        return object;


    }
}
