package it.einjojo.akani.boss.storage.jsonfile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.boss.mob.BossMob;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import it.einjojo.akani.boss.room.RoomData;
import it.einjojo.akani.boss.storage.jsonfile.serializer.*;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

public class GsonFactory {
    private final RequirementFactory requirementFactory;

    public GsonFactory(RequirementFactory requirementFactory) {
        this.requirementFactory = requirementFactory;
    }

    public Gson createGson() {

        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Boss.class, new JsonBossAdapter(requirementFactory))
                .registerTypeAdapter(BoundingBox.class, new JsonBoundingBoxAdapter())
                .registerTypeAdapter(Location.class, new JsonLocationAdapter())
                .registerTypeHierarchyAdapter(ItemStack.class, new JsonItemStackAdapter())
                .registerTypeHierarchyAdapter(BossMob.class, new JsonBossMobAdapter())
                .registerTypeAdapter(RoomData.class, new JsonRoomDataAdapter())
                .create();
    }

}
