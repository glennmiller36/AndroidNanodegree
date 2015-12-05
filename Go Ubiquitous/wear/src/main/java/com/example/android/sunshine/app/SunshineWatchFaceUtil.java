package com.example.android.sunshine.app;

import android.graphics.Color;

/**
 * Created by MillerMac on 11/24/15.
 */
public final class SunshineWatchFaceUtil {

    /**
     * Name of the default interactive mode background color and the ambient mode background color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_BACKGROUND = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_BACKGROUND);

    /**
     * Name of the default interactive mode time digits color and the ambient mode time digits
     * color.
     */
    public static final String COLOR_NAME_INTERACTIVE_TIME_DIGITS = "Black";
    public static final int COLOR_VALUE_INTERACTIVE_TIME_DIGITS =
            parseColor(COLOR_NAME_INTERACTIVE_TIME_DIGITS);

    public static final String COLOR_NAME_AMBIENT_TIME_DIGITS = "White";
    public static final int COLOR_VALUE_AMBIENT_TIME_DIGITS =
            parseColor(COLOR_NAME_AMBIENT_TIME_DIGITS);

    /**
     * Name of the default interactive mode time meridiem color and the ambient mode time meridiem
     * color.
     */
    public static final String COLOR_NAME_INTERACTIVE_TIME_MERIDIEM = "darkgray";
    public static final int COLOR_VALUE_INTERACTIVE_TIME_MERIDIEM =
            parseColor(COLOR_NAME_INTERACTIVE_TIME_MERIDIEM);

    public static final String COLOR_NAME_AMBIENT_TIME_MERIDIEM = "white";
    public static final int COLOR_VALUE_AMBIENT_TIME_MERIDIEM =
            parseColor(COLOR_NAME_AMBIENT_TIME_MERIDIEM);

    /**
     * Name of the default interactive mode oval color and the ambient mode oval color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_OVAL = "#00A8FB";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_OVAL =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_OVAL);

    /**
     * Name of the default interactive mode high color and the ambient mode high color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_HIGH_TEMP = "white";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_HIGH_TEMP =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_HIGH_TEMP);

    /**
     * Name of the default interactive mode low color and the ambient mode low color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_LOW_TEMP = "white";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_LOW_TEMP =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_LOW_TEMP);

    private static int parseColor(String colorName) {
        return Color.parseColor(colorName.toLowerCase());
    }

    private SunshineWatchFaceUtil() { }
}