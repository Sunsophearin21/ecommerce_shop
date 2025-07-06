package com.sunsophearin.shopease.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    // Example: 2025-07-04 12:16
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm";

    public static String format(Date date) {
        if (date == null) return "N/A";
        return new SimpleDateFormat(DEFAULT_PATTERN).format(date);
    }

    // If you want to support custom patterns:
    public static String format(Date date, String pattern) {
        if (date == null) return "N/A";
        return new SimpleDateFormat(pattern).format(date);
    }
}
