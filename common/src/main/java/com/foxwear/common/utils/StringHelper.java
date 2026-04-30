package com.foxwear.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class StringHelper {

    public static String capitalize(String text) {
        text = text != null ? text.trim() : "";

        return text.substring(0, 1).toUpperCase() +
                text.substring(1).toLowerCase();
    }

    public static String toUpper(String text) {
        text = text != null ? text.trim() : "";

        return text.toUpperCase();
    }

    public static String generateSlug(String text) {
        return text.toLowerCase().replace(" ", "-") + "-" + System.currentTimeMillis();
    }

    public static String generateFWNumber() {
        return "FW-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

}
