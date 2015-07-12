package com.glennmiller.spotifystreamer;

import android.app.Application;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

public class SpotifyStreamerApp extends Application {

    // global variable of Artist tracks to share between ArtistTracksActivity and PlayerActivity
    public static ArrayList<Track> arrayOfTracks = new ArrayList<Track>();

    private static SpotifyStreamerApp singleton;

    public static SpotifyStreamerApp getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}