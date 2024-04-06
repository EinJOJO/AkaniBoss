package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class UnlockedWithKeyRequirement implements Requirement {

    public static final String NAME = "unlocked_with_key";
    private final BossFightManager bossFightManager;

    public UnlockedWithKeyRequirement(BossFightManager bossFightManager) {
        this.bossFightManager = bossFightManager;
    }

    @Override
    public boolean check(Boss boss, Player player) {
        return bossFightManager.canEnterBossRoom(player.getUniqueId(), boss.id());
    }

    @Override
    public Component denyMessage(Player player) {
        return Component.text("Das Siegel muss mit einem Schlüssel geöffnet werden!");
    }

    @Override
    public CheckType checkType() {
        return CheckType.ENTER_ROOM;
    }

    public String toString() {
        return NAME;
    }

    @Override
    public String requirementName() {
        return NAME;
    }


}
