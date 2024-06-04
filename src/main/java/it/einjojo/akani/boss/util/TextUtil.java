package it.einjojo.akani.boss.util;

import java.util.Map;

public class TextUtil {

    private static final Map<Character, String> MINIMESSAGE_REPLACEMENTS = Map.ofEntries(
            Map.entry('0', "<black>"),
            Map.entry('1', "<dark_blue>"),
            Map.entry('2', "<dark_green>"),
            Map.entry('3', "<dark_aqua>"),
            Map.entry('4', "<dark_red>"),
            Map.entry('5', "<dark_purple>"),
            Map.entry('6', "<gold>"),
            Map.entry('7', "<gray>"),
            Map.entry('8', "<dark_gray>"),
            Map.entry('9', "<blue>"),
            Map.entry('a', "<green>"),
            Map.entry('b', "<aqua>"),
            Map.entry('c', "<red>"),
            Map.entry('d', "<light_purple>"),
            Map.entry('e', "<yellow>"),
            Map.entry('f', "<white>"),
            Map.entry('k', "<obfuscated>"),
            Map.entry('l', "<bold>"),
            Map.entry('m', "<strikethrough>"),
            Map.entry('n', "<underline>"),
            Map.entry('o', "<italic>"),
            Map.entry('r', "<reset>")
    );

    public static String transformLegacyToMiniMessage(String s) {
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            char c = chars[i];
            if (c == '§') {
                char number = chars[i + 1];
                sb.append(MINIMESSAGE_REPLACEMENTS.get(number));
                i++;
            } else {
                sb.append(c);
            }
        }
        s = sb.toString();
        return s;
    }

}
