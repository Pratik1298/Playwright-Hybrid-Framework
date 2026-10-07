package com.pratik.utils;

import java.util.UUID;

public class TestDataFactory {

    private TestDataFactory() {
    }

    public static String uniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String uniqueEmail() {
        return "pw.test." + uniqueSuffix() + "@example.com";
    }

    public static String uniqueSlug(String prefix) {
        return prefix + "-" + uniqueSuffix();
    }

    public static String strongPassword() {
        return "Pw!" + uniqueSuffix() + "Zz9";
    }
}