package com.fluidminds.android.studiosity.app;

import android.app.Application;

public class StudiosityApp extends Application {
    private static StudiosityApp instance;

    public StudiosityApp() {
        instance = this;
    }

    public static StudiosityApp getInstance() {
        return instance;
    }
}