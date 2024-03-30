package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.fight.BossFightManager;
import it.einjojo.akani.boss.requirement.EntranceRequirement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class KeyUsedEntranceRequirement implements EntranceRequirement {
    public static String NAME = "key_used";
    private final BossFightManager bossFightManager;

    public KeyUsedEntranceRequirement(BossFightManager bossFightManager) {
        this.bossFightManager = bossFightManager;
    }

    @Override
    public String requirementName() {
        return NAME;
    }

    @Override
    public boolean check(Boss boss, Player player) {
        return false;
    }

    @Override
    public Component denyMessage(Player player) {
        return null;
    }
}
