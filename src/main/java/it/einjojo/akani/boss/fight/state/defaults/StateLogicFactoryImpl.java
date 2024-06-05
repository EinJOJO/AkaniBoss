package it.einjojo.akani.boss.fight.state.defaults;

import com.google.common.base.Preconditions;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.fight.BossMobRegistry;
import it.einjojo.akani.boss.fight.state.StateLogic;
import it.einjojo.akani.boss.fight.state.StateLogicFactory;
import it.einjojo.akani.boss.room.RoomManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class StateLogicFactoryImpl implements StateLogicFactory {
    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final RoomManager roomManager;
    @NotNull
    private final BossMobRegistry bossMobRegistry;

    public StateLogicFactoryImpl(@NotNull JavaPlugin plugin, @NotNull RoomManager roomManager, @NotNull BossMobRegistry bossMobRegistry) {
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(roomManager);
        Preconditions.checkNotNull(bossMobRegistry);
        this.plugin = plugin;
        this.bossMobRegistry = bossMobRegistry;
        this.roomManager = roomManager;

    }

    public StateLogic createLogic(BossFight bossFight) {
        return switch (bossFight.state()) {
            case PREPARING -> new PrepareStateLogic(bossFight, roomManager);
            case INTRODUCTION -> new IntroductionStateLogic(bossFight);
            case DISCOVERY -> new DiscoveryStateLogic(plugin, bossMobRegistry, bossFight);
            case CLOSED, FIGHTING, VICTORY, DEFEATED -> new EmptyStateLogic();
            default -> throw new IllegalStateException("Unknown state: " + bossFight.state());
        };
    }

}
