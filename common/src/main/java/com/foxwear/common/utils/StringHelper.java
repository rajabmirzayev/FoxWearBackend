package com.foxwear.common.utils;

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
}
