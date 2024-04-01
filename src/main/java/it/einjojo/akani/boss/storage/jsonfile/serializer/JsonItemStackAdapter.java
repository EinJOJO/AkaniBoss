package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import it.einjojo.akani.boss.util.Base64ItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class JsonItemStackAdapter implements Adapter<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Base64ItemStack.decode(json.getAsString());
        } catch (Base64ItemStack.Base64ConvertException exception) {
            throw new JsonParseException("Invalid base64 item: " + json.getAsString(), exception);
        }
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Base64ItemStack.encode(src));
    }
}
