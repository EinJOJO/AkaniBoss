package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.lang.reflect.Type;

public class JsonKyoriComponentAdapter implements Adapter<Component> {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return miniMessage.deserialize(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(miniMessage.serialize(component));
    }
}
