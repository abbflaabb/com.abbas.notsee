package com.abbas.notsee.core;

import net.md_5.bungee.api.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class ColorUtils {
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public static String translateColorCodes(String text) {
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));
        StringBuilder finalText = new StringBuilder();

        for(int i = 0; i < texts.length; i++) {
            if(texts[i].equalsIgnoreCase("&")) {
                i++;
                // In 1.8.8, hex colors aren't supported, so we'll handle only standard color codes
                finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }

    public static List<String> translateColorCodes(List<String> lines) {
        List<String> coloredLines = new ArrayList<>();

        for (String line: lines) {
            coloredLines.add(translateColorCodes(line));
        }
        return coloredLines;
    }
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
