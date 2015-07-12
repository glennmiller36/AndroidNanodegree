package com.glennmiller.spotifystreamer;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class PlayerActivity extends ActionBarActivity {

    private static final String TAG_PLAYERACTIVITY_FRAGMENT = "playeractivity_fragment";

    private PlayerActivityFragment _playerActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        _playerActivityFragment = (PlayerActivityFragment) fm.findFragmentByTag(TAG_PLAYERACTIVITY_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (_playerActivityFragment == null) {
            _playerActivityFragment = new PlayerActivityFragment();

            ScrollView fragContainer = (ScrollView) findViewById(R.id.FragmentContainer);

            fm.beginTransaction().add(fragContainer.getId(), _playerActivityFragment, TAG_PLAYERACTIVITY_FRAGMENT).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
