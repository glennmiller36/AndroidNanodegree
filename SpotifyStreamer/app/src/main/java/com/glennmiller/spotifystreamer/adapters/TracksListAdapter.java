package com.glennmiller.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glennmiller.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

/**
 * ArrayAdapter for the Artists Track list.
 */
public class TracksListAdapter extends ArrayAdapter<Track> {

    // Recycle views
    private ImageView _albumImageView;
    private TextView _artistNameText;
    private TextView _albumNameText;

    public TracksListAdapter(Context context, ArrayList<Track> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Track track = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_artist_track, parent, false);

            _albumImageView = (ImageView) convertView.findViewById(R.id.imageAlbum);
            _artistNameText = (TextView) convertView.findViewById(R.id.textArtistName);
            _albumNameText = (TextView) convertView.findViewById(R.id.textAlbumName);
        }

        if (track.album.images.size() > 1)
            Picasso.with(convertView.getContext()).load(track.album.images.get(1).url).into(_albumImageView);   // 300 x 300
        else
            Picasso.with(convertView.getContext()).load(android.R.drawable.ic_menu_gallery).into(_albumImageView);

        _artistNameText.setText(track.name);
        _albumNameText.setText(track.album.name);

        return convertView;
    }
}
