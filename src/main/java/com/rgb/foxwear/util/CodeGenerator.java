package com.rgb.foxwear.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerator {
    public static String generateSku() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        int randomPart = ThreadLocalRandom.current().nextInt(100, 1000);

        return "SKU-" + timestamp + "-" + randomPart;
    }
}
