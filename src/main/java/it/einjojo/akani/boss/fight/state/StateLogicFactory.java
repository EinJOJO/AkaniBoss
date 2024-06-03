package it.einjojo.akani.boss.fight.state;

import com.google.common.base.Preconditions;
import it.einjojo.akani.boss.fight.BossFight;
import it.einjojo.akani.boss.room.RoomManager;
import org.jetbrains.annotations.NotNull;

public class StateLogicFactory {
    @NotNull
    private final RoomManager roomManager;

    public StateLogicFactory(@NotNull RoomManager roomManager) {
        Preconditions.checkNotNull(roomManager);
        this.roomManager = roomManager;

    }

    public StateLogic createLogic(BossFight bossFight) {
        return switch (bossFight.state()) {
            case PREPARING -> new PrepareStateLogic(bossFight, roomManager);
            case INTRODUCTION -> new IntroductionStateLogic(bossFight);
            case DISCOVERY, ENDING, FIGHTING -> new EmptyStateLogic();
            default -> throw new IllegalStateException("Unknown state: " + bossFight.state());
        };
    }

}
