package it.einjojo.akani.boss.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextUtil {
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();


    public static MiniMessage miniMessage() {
        return MINI_MESSAGE;
    }

    public static LegacyComponentSerializer legacyComponentSerializer() {
        return LEGACY_COMPONENT_SERIALIZER;
    }

    public static String transformMiniMessageToLegacy(String s) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(MINI_MESSAGE.deserialize(s));
    }

    public static String transformLegacyToMiniMessage(String s) {
        return MINI_MESSAGE.serialize(LEGACY_COMPONENT_SERIALIZER.deserialize(s));
    }




}
