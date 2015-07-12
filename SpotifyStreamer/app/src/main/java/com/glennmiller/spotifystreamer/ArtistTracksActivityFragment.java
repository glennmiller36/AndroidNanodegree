package com.glennmiller.spotifystreamer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.glennmiller.spotifystreamer.adapters.TracksListAdapter;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTracksActivityFragment extends Fragment {

    ProgressBar _progressBar;
    ListView _artistList;
    TextView _messageText;
    TracksListAdapter _adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment when activity is re-initlized
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_tracks,
                container, false);

        String artistID = getActivity().getIntent().getExtras().getString("ID");

        _progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        _messageText = (TextView) view.findViewById(R.id.textMessage);
        _messageText.setAllCaps(true);

        // global variable of Artist tracks to share between ArtistTracksActivity and PlayerActivity
        SpotifyStreamerApp.arrayOfTracks.clear();

        // create the adapter to convert the array to views
        _adapter = new TracksListAdapter(getActivity(), SpotifyStreamerApp.arrayOfTracks);

        // attach the adapter to the ListView
        _artistList = (ListView) view.findViewById(R.id.listTopTracks);
        _artistList.setAdapter(_adapter);
        _artistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
            intent.putExtra("TRACK_POSITION", position);

            startActivity(intent);
            }
        });

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        Map<String, Object> options = new HashMap<>();
        options.put("country", "US");

        spotify.getArtistTopTrack(artistID, options, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, retrofit.client.Response response) {
                _adapter.clear();

                for (Track track : tracks.tracks) {
                    _adapter.add(track);
                }

                _progressBar.setVisibility(View.INVISIBLE);

                if (tracks.tracks.size() == 0) {
                    _messageText.setText(R.string.listIsEmpty);
                    _messageText.setVisibility(View.VISIBLE);
                }
                else {
                    _messageText.setVisibility(View.INVISIBLE);
                    _artistList.setVisibility(View.VISIBLE);
                }

                _adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                _progressBar.setVisibility(View.INVISIBLE);

                _messageText.setText(error.getMessage());
                _messageText.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}
