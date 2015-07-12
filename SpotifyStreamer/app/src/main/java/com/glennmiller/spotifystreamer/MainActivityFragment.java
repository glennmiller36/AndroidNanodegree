package com.glennmiller.spotifystreamer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.glennmiller.spotifystreamer.adapters.ArtistsListAdapter;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener {

    ProgressBar _progressBar;
    ListView _artistList;
    ArrayList<Artist> _arrayOfArtists = new ArrayList<Artist>();
    TextView _messageText;
    ArtistsListAdapter _adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment when activity is re-initlized
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main,
                container, false);

        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        _progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        _messageText = (TextView) view.findViewById(R.id.textMessage);
        _messageText.setAllCaps(true);

        // use Reflection to hide the search magnifying glass
        int searchImgId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        v.setImageDrawable(transparentDrawable);

        // create the adapter to convert the array to views
        _adapter = new ArtistsListAdapter(getActivity(), _arrayOfArtists);

        // attach the adapter to the ListView
        _artistList = (ListView) view.findViewById(R.id.listArtists);
        _artistList.setAdapter(_adapter);
        _artistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist item = _adapter.getItem(position);

                Intent intent = new Intent(getActivity(), ArtistTracksActivity.class);
                intent.putExtra("ArtistName", item.name);
                intent.putExtra("ID", item.id);

                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        _arrayOfArtists.clear();
        _adapter.notifyDataSetChanged();

        if (query.length() > 0) {

            _artistList.setVisibility(View.INVISIBLE);
            _progressBar.setVisibility(View.VISIBLE);
            _messageText.setVisibility(View.INVISIBLE);

            spotify.searchArtists(query, new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, retrofit.client.Response response) {

                    for (kaaes.spotify.webapi.android.models.Artist artist : artistsPager.artists.items) {
                        _adapter.add(artist);
                    }
                    _adapter.notifyDataSetChanged();

                    _progressBar.setVisibility(View.INVISIBLE);

                    if (artistsPager.artists.items.size() == 0) {
                        _messageText.setText(R.string.listIsEmpty);
                        _messageText.setVisibility(View.VISIBLE);
                    } else {
                        _messageText.setVisibility(View.INVISIBLE);
                        _artistList.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    _progressBar.setVisibility(View.INVISIBLE);

                    _messageText.setText(error.getMessage());
                    _messageText.setVisibility(View.VISIBLE);

                }
            });
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
