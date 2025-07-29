package com.zolqid.zolesle.util;

import java.security.SecureRandom;

public class KodUretici {

    private static final String HARFLER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String uret() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(HARFLER.charAt(random.nextInt(HARFLER.length())));
        }
        return sb.toString();
    }
}
