package com.fluidminds.android.studiosity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilities to convert data.
 */
public class Converters {

    public static Date stringToDate(String string) {
        Date date = null;
        if (string == null || string.isEmpty())
            return null;

        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
