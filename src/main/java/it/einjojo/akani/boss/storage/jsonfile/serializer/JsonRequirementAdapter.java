package it.einjojo.akani.boss.storage.jsonfile.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import it.einjojo.akani.boss.requirement.Requirement;
import it.einjojo.akani.boss.requirement.RequirementFactory;

import java.lang.reflect.Type;

public class JsonRequirementAdapter implements Adapter<Requirement> {
    private final RequirementFactory requirementFactory;

    public JsonRequirementAdapter(RequirementFactory requirementFactory) {
        this.requirementFactory = requirementFactory;
    }

    @Override
    public Requirement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return requirementFactory.parseRequirement(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Requirement requirement, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(requirement.toString());
    }
}
