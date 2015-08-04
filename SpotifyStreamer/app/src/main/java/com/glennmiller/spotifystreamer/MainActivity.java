package com.glennmiller.spotifystreamer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

    ArtistTracksActivityFragment _tabletTracksFragment;

    public ArtistTracksActivityFragment getTabletTracksFragment() {
        return _tabletTracksFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Is this a tablet layout? i.e. search and tracks fragment on the same activity
        _tabletTracksFragment = (ArtistTracksActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTracks);
    }
}
