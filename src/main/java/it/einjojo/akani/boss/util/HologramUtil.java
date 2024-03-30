package it.einjojo.akani.boss.util;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import it.einjojo.akani.boss.boss.Boss;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;

public class HologramUtil {
    private static final String HOLOGRAM_PREFIX = "boss_";

    public static void removeBossHologram(Boss boss) {
        DHAPI.removeHologram(HOLOGRAM_PREFIX + boss.id());
    }

    public static void createBossHologram(Location location, Boss boss) {
        removeBossHologram(boss);
        Hologram hologram = DHAPI.createHologram(HOLOGRAM_PREFIX + boss.id(), location);
        DHAPI.addHologramLine(hologram, "§7§m---====§r§7[ §c§lBossraum §7]§m====---");
        DHAPI.addHologramLine(hologram, "");
        DHAPI.addHologramLine(hologram, boss.keyItem());
        DHAPI.addHologramLine(hologram, "§7Boss: " + LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(boss.bossName())));
        DHAPI.addHologramLine(hologram, "§7Level: §f" + boss.bossLevel());
        DHAPI.addHologramLine(hologram, "§7Schwierigkeit: " + boss.difficulty().legacyText());
        DHAPI.addHologramLine(hologram, "");


    }

}
