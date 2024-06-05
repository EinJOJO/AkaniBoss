package it.einjojo.akani.boss.requirement.defaults;

import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class PermissionRequirement implements Requirement {
    public static final String NAME = "permission";
    private final String permission;
    private final Component denyMessage;

    public PermissionRequirement(String permission, Component denyMessage) {
        this.permission = permission;
        this.denyMessage = denyMessage;
    }



    @Override
    public String toString() {
        return NAME + ARGUMENT_SPLITTER + permission + ARGUMENT_SPLITTER + MiniMessage.miniMessage().serialize(denyMessage);

    }

    @Override
    public boolean check(Boss boss, Player player) {
        return player.hasPermission(permission);
    }

    @Override
    public Component denyMessage(Player player) {
        return denyMessage;
    }
}
