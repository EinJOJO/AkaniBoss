package it.einjojo.akani.boss.util;

public class TextUtil {

    public static String transformLegacyToMiniMessage(String s) {
        s = s.replaceAll("§a", "<green>");
        s = s.replaceAll("§b", "<aqua>");
        s = s.replaceAll("§c", "<red>");
        s = s.replaceAll("§d", "<light_purple>");
        s = s.replaceAll("§e", "<yellow>");
        s = s.replaceAll("§f", "<white>");
        s = s.replaceAll("§0", "<black>");
        s = s.replaceAll("§1", "<dark_blue>");
        s = s.replaceAll("§2", "<dark_green>");
        s = s.replaceAll("§3", "<dark_aqua>");
        s = s.replaceAll("§4", "<dark_red>");
        s = s.replaceAll("§5", "<dark_purple>");
        s = s.replaceAll("§6", "<gold>");
        s = s.replaceAll("§7", "<gray>");
        s = s.replaceAll("§8", "<dark_gray>");
        s = s.replaceAll("§9", "<blue>");
        s = s.replaceAll("§k", "<obfuscated>");
        s = s.replaceAll("§l", "<bold>");
        s = s.replaceAll("§m", "<strikethrough>");
        s = s.replaceAll("§n", "<underline>");
        s = s.replaceAll("§o", "<italic>");
        s = s.replaceAll("§r", "<reset>");
        return s;
    }

}
