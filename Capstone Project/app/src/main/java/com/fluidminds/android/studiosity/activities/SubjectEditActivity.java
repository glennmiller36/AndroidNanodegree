package com.fluidminds.android.studiosity.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.eventbus.ThemeColorChangedEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * An activity to Add or Edit an individual school Subject.
 */
public class SubjectEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_edit);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Register as a subscriber
        mEventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        // Unregister
        mEventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEvent(ThemeColorChangedEvent event){
        colorizeToolbar(event.getColor());
    }
}
