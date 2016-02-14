package com.fluidminds.android.studiosity.utils;

import android.support.v4.graphics.ColorUtils;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * The ThemeColor class defines methods for showing available colors and converting color ints to literal name.
 */
public class ThemeColor {
    // Color500
    public static final int RED         = 0xFFF44336;
    public static final int PINK        = 0xFFE91E63;
    public static final int PURPLE      = 0xFF9C27B0;
    public static final int DEEPPURPLE  = 0xFF673AB7;
    public static final int INDIGO      = 0xFF3F51B5;
    public static final int BLUE        = 0xFF2196F3;
    public static final int LIGHTBLUE   = 0xFF03A9F4;
    public static final int CYAN        = 0xFF00BCD4;
    public static final int TEAL        = 0xFF009688;
    public static final int GREEN       = 0xFF4CAF50;
    public static final int LIGHTGREEN  = 0xFF8BC34A;
    public static final int LIME        = 0xFFCDDC39;
    public static final int YELLOW      = 0xFFFFEB3B;
    public static final int AMBER       = 0xFFFFC107;
    public static final int ORANGE      = 0xFFFF9800;
    public static final int DEEPORANGE  = 0xFFFF5722;
    public static final int BROWN       = 0xFF795548;
    public static final int GREY        = 0xFF9E9E9E;
    public static final int BLUEGREY    = 0xFF607D8B;

    private static final LinkedHashMap<Integer, String> sColorNameMap;

    private static Random sRandom  = new Random();

    static {
        sColorNameMap = new LinkedHashMap<>();
        sColorNameMap.put(RED, "Red");
        sColorNameMap.put(PINK, "Pink");
        sColorNameMap.put(PURPLE, "Purple");
        sColorNameMap.put(DEEPPURPLE, "Deep Purple");
        sColorNameMap.put(INDIGO, "Indigo");
        sColorNameMap.put(BLUE, "Blue");
        sColorNameMap.put(LIGHTBLUE, "Light Blue");
        sColorNameMap.put(CYAN, "Cyan");
        sColorNameMap.put(TEAL, "Teal");
        sColorNameMap.put(GREEN, "Green");
        sColorNameMap.put(LIGHTGREEN, "Light Green");
        sColorNameMap.put(LIME, "Lime");
        sColorNameMap.put(YELLOW, "Yellow");
        sColorNameMap.put(AMBER, "Amber");
        sColorNameMap.put(ORANGE, "Orange");
        sColorNameMap.put(DEEPORANGE, "Deep Orange");
        sColorNameMap.put(BROWN, "Brown");
        sColorNameMap.put(GREY, "Grey");
        sColorNameMap.put(BLUEGREY, "Blue Grey");
    }

    /**
     * Convert the int color to its literal name.
     */
    public static String getColorName(int color) {
        return sColorNameMap.get(color);
    }

    /**
     * Returns the list of supported colors.
     */
    public static LinkedHashMap<Integer, String> getColor500List() {
        return sColorNameMap;
    }

    /**
     * Determine if White is the appropriate contrast color for the requested color.
     */
    public static boolean isWhiteContrastColor(int color) {
        if (ColorUtils.calculateLuminance(color) > 0.250)
            return false; // use black
        else
            return true;  // use white
    }

    /**
     * Generate a random color for a new SubjectModel.
     */
    public static Integer generateRandomColor() {
        int min = 0;
        int max = getColor500List().size();

        int random = sRandom.nextInt(max - min) + min;

        return (Integer) sColorNameMap.keySet().toArray()[random];
    }
}
