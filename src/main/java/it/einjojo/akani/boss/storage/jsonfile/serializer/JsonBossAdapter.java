package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.*;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.boss.BossDifficulty;
import it.einjojo.akani.boss.requirement.Requirement;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class JsonBossAdapter implements Adapter<Boss> {
    private final RequirementFactory requirementFactory;
    private final Logger logger = Logger.getLogger("JsonBossAdapter");

    public JsonBossAdapter(RequirementFactory requirementFactory) {
        this.requirementFactory = requirementFactory;
    }

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
            requirements.add(requirement.toString());
        }
        object.add("requirements", requirements);
        object.add("boundingBox", context.serialize(src.dungeonEntrance()));
        object.add("item", context.serialize(src.keyItem()));
        return object;
    }

    @Override
    public Boss deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        List<Requirement> requirements = new LinkedList<>();
        for (JsonElement entranceElement : object.getAsJsonArray("requirements")) {
            Requirement req = requirementFactory.parseRequirement(entranceElement.getAsString());
            if (req != null)
                requirements.add(req);
            else
                logger.warning("Invalid Requirement: " + entranceElement.getAsString());


        }
        return new Boss(
                object.get("id").getAsString(),
                object.get("name").getAsString(),
                object.get("room-template").getAsString(),
                object.get("level").getAsInt(),
                BossDifficulty.valueOf(object.get("difficulty").getAsString()),
                context.deserialize(object.get("key-location"), Location.class),
                requirements,
                context.deserialize(object.get("boundingBox"), BoundingBox.class),
                context.deserialize(object.get("item"), ItemStack.class)
        );
    }
}
