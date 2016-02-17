package com.fluidminds.android.studiosity.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.fluidminds.android.studiosity.utils.CustomizeToolbarHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * Properties and Methods common to all Activities.
 */
public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected Menu mMenu;
    protected EventBus mEventBus = EventBus.getDefault();

    public void colorizeToolbar(Integer color) {
        CustomizeToolbarHelper.colorizeToolbar(this, mToolbar, mMenu, color);
    }
}
