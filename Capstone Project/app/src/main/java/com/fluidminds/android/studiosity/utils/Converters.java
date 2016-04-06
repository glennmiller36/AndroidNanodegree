package com.fluidminds.android.studiosity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utilities to convert data.
 */
public class Converters {

    private static final String sDATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static Date stringToDate(String string) {
        Date date = null;
        if (string == null || string.isEmpty())
            return null;

        try {
            date = new SimpleDateFormat(sDATEFORMAT).parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date) {
        return dateToString(date, sDATEFORMAT);
    }

    public static String dateToString(Date date, String format) {
        String string = null;
        if (date == null)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(date);
    }

    /**
     * Converts accuracy percentage to color - like a stop light. Green means you
     * got it; Yellow means it needs some work; Red means you don't know it.
     */
    public static int accuracyPercentToColor(Integer accuracy) {
        if (accuracy >= 80)
            return ThemeColor.GREEN;    // Got It!
        else if (accuracy >= 60)
            return ThemeColor.YELLOW;   // Caution - needs work
        else
            return ThemeColor.RED;      // Don't know it
    }
}