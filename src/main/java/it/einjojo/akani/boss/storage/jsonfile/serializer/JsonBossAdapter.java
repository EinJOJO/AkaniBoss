package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.boss.BossDifficulty;
import it.einjojo.akani.boss.boss.mob.BossMob;
import it.einjojo.akani.boss.boss.mob.VanillaMobFactory;
import it.einjojo.akani.boss.loot.Loot;
import it.einjojo.akani.boss.requirement.Requirement;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class JsonBossAdapter implements Adapter<Boss> {
    private final Logger logger = Logger.getLogger("JsonBossAdapter");


    @Override
    public JsonElement serialize(Boss src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("id", src.id());
        object.addProperty("name", src.bossName());
        object.addProperty("room-template", src.roomTemplateName());
        object.addProperty("level", src.bossLevel());
        object.addProperty("difficulty", src.difficulty().name());
        object.add("key-location", context.serialize(src.keyRedeemLocation()));
        JsonArray requirements = new JsonArray();
        for (Requirement requirement : src.requirements()) {
            requirements.add(context.serialize(requirement));
        }
        object.add("requirements", requirements);
        object.add("boundingBox", context.serialize(src.dungeonEntrance()));
        object.add("itemStack", context.serialize(src.keyItem()));
        object.add("mob", context.serialize(src.bossMob()));
        JsonArray lootArray = new JsonArray();
        for (Loot loot : src.lootList()) {
            lootArray.add(context.serialize(loot));
        }
        object.add("loot", lootArray);

        return object;
    }

    @Override
    public Boss deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        List<Requirement> requirements = new LinkedList<>();
        for (JsonElement entranceElement : object.getAsJsonArray("requirements")) {
            Requirement req = context.deserialize(entranceElement, Requirement.class);
            if (req != null)
                requirements.add(req);
            else
                logger.warning("Invalid Requirement: " + entranceElement.getAsString());
        }

        List<Loot> lootList = new LinkedList<>();
        if (object.has("loot")) {
            for (JsonElement lootElement : object.getAsJsonArray("loot")) {
                Loot loot = context.deserialize(lootElement, Loot.class);
                if (loot != null)
                    lootList.add(loot);
                else
                    logger.warning("Invalid Loot: " + lootElement.getAsString());
            }
        } else {
            logger.warning("No Loot found for boss: " + object.get("name").getAsString());
        }

        Location keyLocation = context.deserialize(object.get("key-location"), Location.class);
        if (keyLocation.getWorld() == null) {
            logger.warning("Invalid world for key location: " + keyLocation.getWorld().getName());
            return null;
        }
        return new Boss(
                object.get("id").getAsString(),
                object.get("name").getAsString(),
                object.get("room-template").getAsString(),
                object.get("level").getAsInt(),
                BossDifficulty.valueOf(object.get("difficulty").getAsString()),
                keyLocation,
                requirements,
                context.deserialize(object.get("boundingBox"), BoundingBox.class),
                context.deserialize(object.get("itemStack"), ItemStack.class),
                object.has("mob") ? context.deserialize(object.get("mob"), BossMob.class) : new VanillaMobFactory().createBossMob("SLIME"),
                lootList
        );
    }
}
