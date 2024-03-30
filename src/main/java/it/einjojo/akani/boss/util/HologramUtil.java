package it.einjojo.akani.boss.util;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import it.einjojo.akani.boss.boss.Boss;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;

public class HologramUtil {

    public static void createBossHologram(Location location, Boss boss) {
        Hologram hologram = DHAPI.createHologram(boss.id(), location);
        DHAPI.addHologramLine(hologram, "§7§m=====[§r §6§lBossraum §7§m]=====");
        DHAPI.addHologramLine(hologram, "");
        DHAPI.addHologramLine(hologram, LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(boss.bossName())));
        DHAPI.addHologramLine(hologram, "§7Level: §f" + boss.bossLevel());
        DHAPI.addHologramLine(hologram, "§7Schwierigkeit: " + boss.difficulty().name());
        DHAPI.addHologramLine(hologram, "");
        DHAPI.addHologramLine(hologram, "§7§m=====[§r §6§lBossraum §7§m]=====");

    }

}
