package io.github.AzmiiD.aProfile.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    public static String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColor(String message) {
        if (message == null) return "";
        return ChatColor.stripColor(message);
    }

    public static String[] colorizeArray(String[] messages) {
        if (messages == null) return new String[0];

        String[] colorized = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            colorized[i] = colorize(messages[i]);
        }
        return colorized;
    }
}