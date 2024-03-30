package it.einjojo.akani.boss.boss;

import it.einjojo.akani.boss.requirement.EntranceRequirement;
import it.einjojo.akani.boss.requirement.KeyReedemRequirement;
import it.einjojo.akani.boss.util.BoundaryBox;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;

import java.util.List;

/**
 * Represents a boss in the game.
 *
 * @param entranceRequirements  the requirements to enter the boss room.
 * @param keyRedeemRequirements the requirements to redeem a key.
 * @param roomTemplateName      the name of the room template to use.
 * @param keyRedeemLocation     the location to redeem the key.
 * @param entranceBox           the box to enter the boss room. (Used for teleport detection)
 * @param bossName              the name of the boss.
 * @param bossLevel             the level of the boss.
 * @param difficulty            the difficulty of the boss.
 */
public record Boss(
        String id,
        String bossName,
        String roomTemplateName,
        int bossLevel,
        BossDifficulty difficulty,
        Location keyRedeemLocation,
        List<EntranceRequirement> entranceRequirements,
        List<KeyReedemRequirement> keyRedeemRequirements,
        BoundaryBox entranceBox
) {

    public Component bossNameComponent() {
        return MiniMessage.miniMessage().deserialize(bossName);
    }

    public Component debugInfoComponent() {
        var l1 = Component.text("ID: ").append(Component.text(id)).appendNewline();
        var l2 = Component.text("Name: ").append(bossNameComponent()).appendNewline();
        var l3 = Component.text("Level: ").append(Component.text(bossLevel)).appendNewline();
        var l4 = Component.text("Difficulty: ").append(Component.text(difficulty.name())).appendNewline();
        var l5 = Component.text("Key Redeem Location: ").append(Component.text(keyRedeemLocation.toString())).appendNewline();
        var l6 = Component.text("Entrance Box: ").append(Component.text(entranceBox.toString())).appendNewline();
        var l7 = Component.text("Room Template: ").append(Component.text(roomTemplateName)).appendNewline();
        var l8 = Component.text("Entrance Requirements: ").appendNewline();
        for (var requirement : entranceRequirements) {
            l8 = l8.append(Component.text(" " + requirement.toString() + ", ")).appendNewline();
        }
        var l9 = Component.text("Key Redeem Requirements: ").appendNewline();
        for (var requirement : keyRedeemRequirements) {
            l9 = l9.append(Component.text(" " + requirement.toString() + ", ")).appendNewline();
        }
        return Component.text().append(l1).append(l2).append(l3).append(l4).append(l5).append(l6)
                .append(l7).append(l8).append(l9).build();

    }

    public BossBuilder toBuilder() {
        return new BossBuilder(this);
    }
}
