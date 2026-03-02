package com.rgb.foxwear.util;

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
}
