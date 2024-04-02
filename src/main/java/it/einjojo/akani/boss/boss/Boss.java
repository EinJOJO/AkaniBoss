package it.einjojo.akani.boss.boss;

import it.einjojo.akani.boss.boss.mob.BossMob;
import it.einjojo.akani.boss.requirement.Requirement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.List;

/**
 * Represents a boss in the game.
 *
 * @param id                the id of the boss.
 * @param requirements      the requirements to enter the boss room.
 * @param roomTemplateName  the name of the room template to use.
 * @param keyRedeemLocation the location to redeem the key.
 * @param dungeonEntrance   the box to enter the boss room. (Used for teleport detection)
 * @param bossName          the name of the boss.
 * @param bossLevel         the level of the boss.
 * @param difficulty        the difficulty of the boss.
 */
public record Boss(
        String id,
        String bossName,
        String roomTemplateName,
        int bossLevel,
        BossDifficulty difficulty,
        Location keyRedeemLocation,
        List<Requirement> requirements,
        BoundingBox dungeonEntrance,
        ItemStack keyItem,
        BossMob<?> bossMob
) {

    public Component bossNameComponent() {
        return MiniMessage.miniMessage().deserialize(bossName);
    }

    public Component debugInfoComponent() {
        NamedTextColor GRAY = NamedTextColor.GRAY;

        NamedTextColor PRIMARY = NamedTextColor.LIGHT_PURPLE;
        var l1 = Component.text("ID: ").color(GRAY).append(Component.text(id).color(PRIMARY)).appendNewline();
        var l2 = Component.text("Name: ").append(bossNameComponent()).appendNewline();
        var l3 = Component.text("Level: ").color(GRAY).append(Component.text(bossLevel).color(PRIMARY)).appendNewline();
        var l4 = Component.text("Difficulty: ").append(difficulty.toComponent()).appendNewline();
        var l5 = Component.text("Key Redeem Location: ").color(GRAY).append(Component.text("%f.1 %f.1 %f.1".formatted(keyRedeemLocation.getX(), keyRedeemLocation.getY(), keyRedeemLocation.getZ())).color(PRIMARY)).appendNewline();
        var l6 = Component.text("Entrance Box: ").append(Component.text(dungeonEntrance.toString()).color(PRIMARY)).appendNewline();
        var l7 = Component.text("Room Template: ").color(GRAY).append(Component.text(roomTemplateName).color(PRIMARY)).appendNewline();
        var l8 = Component.text(" Requirements: ").appendNewline();
        for (var requirement : requirements) {
            l8 = l8.append(Component.text(" [" + requirement.checkType().name() + "] " + requirement + ", ")).color(GRAY).appendNewline();
        }

        return Component.text().append(l1).append(l2).append(l3).append(l4).append(l5).append(l6)
                .append(l7).append(l8).build();
    }

    public Requirement testRequirements(Player player) {
        for (Requirement requirement : requirements) {
            if (!requirement.check(this, player)) {
                return requirement;
            }
        }
        return null;
    }

    public boolean checkKey(ItemStack is) {
        return keyItem.isSimilar(is);
    }


    public BossBuilder toBuilder() {
        return new BossBuilder(this);
    }
}
