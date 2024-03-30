package it.einjojo.akani.boss.util;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import it.einjojo.akani.boss.boss.Boss;
import org.bukkit.Location;

public class HologramUtil {

    public static void createBossHologram(Location location, Boss boss) {
        Hologram hologram = DHAPI.createHologram(boss.id(), location);
        DHAPI.addHologramLine(hologram, boss.bossName());
        DHAPI.addHologramLine(hologram, "<gray>Level: <white>" + boss.bossLevel());
        DHAPI.addHologramLine(hologram, "<gray>Schwierigkeit: " + boss.difficulty().name());

    }

}
