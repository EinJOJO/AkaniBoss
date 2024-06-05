package it.einjojo.akani.boss.boss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public enum BossDifficulty {
    EASY("§a§lEinfach"),
    NORMAL("§e§lMittel"),
    HARD("§c§lHard"),
    VERY_HARD("§4§lSehr schwer"),
    EXTREME("§5§lExtrem");

    private final String legacyText;

    BossDifficulty(String legacyText) {
        this.legacyText = legacyText;
    }


    /**
     * @return legacy for hologram stuff.
     */
    public String legacyText() {
        return legacyText;
    }

    public Component toComponent() {
        return LegacyComponentSerializer.legacySection().deserialize(legacyText);
    }

}
