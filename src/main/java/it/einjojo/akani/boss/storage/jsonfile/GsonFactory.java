package it.einjojo.akani.boss.storage.jsonfile;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.boss.mob.BossMob;
import it.einjojo.akani.boss.loot.Loot;
import it.einjojo.akani.boss.loot.LootFactory;
import it.einjojo.akani.boss.requirement.Requirement;
import it.einjojo.akani.boss.requirement.RequirementFactory;
import it.einjojo.akani.boss.room.RoomData;
import it.einjojo.akani.boss.storage.jsonfile.serializer.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class GsonFactory {

    public Gson createGson(@NotNull RequirementFactory requirementFactory, LootFactory lootFactory) {
        Preconditions.checkNotNull(requirementFactory);
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Boss.class, new JsonBossAdapter())
                .registerTypeAdapter(BoundingBox.class, new JsonBoundingBoxAdapter())
                .registerTypeHierarchyAdapter(Loot.class, new JsonLootAdapter(lootFactory))
                .registerTypeAdapter(Location.class, new JsonLocationAdapter())
                .registerTypeHierarchyAdapter(Component.class, new JsonKyoriComponentAdapter())
                .registerTypeHierarchyAdapter(Requirement.class, new JsonRequirementAdapter(requirementFactory))
                .registerTypeHierarchyAdapter(ItemStack.class, new JsonItemStackAdapter())
                .registerTypeHierarchyAdapter(BossMob.class, new JsonBossMobAdapter())
                .registerTypeAdapter(RoomData.class, new JsonRoomDataAdapter())
                .create();
    }

}
