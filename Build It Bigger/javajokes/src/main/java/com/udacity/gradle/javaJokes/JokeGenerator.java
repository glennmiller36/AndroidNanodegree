package com.udacity.gradle.javaJokes;

import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class JokeGenerator {

    public String tellJoke() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Jokes");

        int min = 1;
        int max = 13;

        Random r = new Random();
        int index = r.nextInt(max - min + 1) + min;

        return resourceBundle.getString("joke" + String.valueOf(index));
    }
}
