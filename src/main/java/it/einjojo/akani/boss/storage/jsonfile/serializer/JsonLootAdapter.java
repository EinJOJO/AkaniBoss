package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import it.einjojo.akani.boss.loot.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class JsonLootAdapter implements Adapter<Loot> {

    private final LootFactory lootFactory;

    public JsonLootAdapter(LootFactory lootFactory) {
        this.lootFactory = lootFactory;
    }

    @Override
    public Loot deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String id = object.get("id").getAsString();
        boolean isFixedItem = id.equals(LootFixedItem.class.getSimpleName());
        boolean isChancedItem = id.equals(LootChancedItem.class.getSimpleName());
        // ITEM
        if (isFixedItem || isChancedItem) {
            ItemStack itemStack = jsonDeserializationContext.deserialize(object.get("itemStack"), ItemStack.class);
            if (isChancedItem) {
                var chance = object.get("chance").getAsFloat();
                return lootFactory.createChancedItem(itemStack, chance);
            } else {
                return lootFactory.createFixedItem(itemStack);
            }
        }
        boolean isFixedMoney = id.equals(LootFixedMoney.class.getSimpleName());
        boolean isRangedMoney = id.equals(LootRangeMoney.class.getSimpleName());
        if (isFixedMoney || isRangedMoney) {
            int money = object.get("money").getAsInt();
            if (isRangedMoney) {
                int maxMoney = object.get("max-money").getAsInt();
                return lootFactory.createRangeMoney(money, maxMoney);
            } else {
                return lootFactory.createFixedMoney(money);
            }
        }
        throw new JsonParseException("Invalid loot id: " + id);
    }

    @Override
    public JsonElement serialize(Loot loot, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("id", loot.getClass().getSimpleName());
        if (loot instanceof LootFixedItem fixedItem) {
            object.add("itemStack", jsonSerializationContext.serialize(fixedItem.itemStack()));
        } else if (loot instanceof LootChancedItem chancedItem) {
            object.add("itemStack", jsonSerializationContext.serialize(chancedItem.itemStack()));
            object.addProperty("chance", chancedItem.chance());
        } else if (loot instanceof LootFixedMoney fixedMoney) {
            object.addProperty("money", fixedMoney.amount());
        } else if (loot instanceof LootRangeMoney rangeMoney) {
            object.addProperty("money", rangeMoney.min());
            object.addProperty("max-money", rangeMoney.max());
        } else {
            throw new JsonParseException("Invalid loot type: " + loot.getClass().getSimpleName());
        }
        return object;
    }
}
