package it.einjojo.akani.boss.util;

public class ParseUtil {


    /**
     * @param arg     the string to parse
     * @param argName the name of the argument
     * @return the parsed double
     * @throws IllegalArgumentException if the given string is not a valid double.
     */
    public static double parseDouble(String arg, String argName) {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number for " + argName + " in min_players requirement");
        }
    }

    /**
     * @param arg     the string to parse
     * @param argName the name of the argument
     * @return the parsed integer
     * @throws IllegalArgumentException if the given string is not a valid integer.
     */
    public static int parseInt(String arg, String argName) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number for " + argName + " requirement");
        }
    }
}
