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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * ArrayAdapter for the Artists search results.
 */
public class ArtistsListAdapter extends ArrayAdapter<Artist> {

    // Recycle views
    private ImageView _albumImageView;
    private TextView _artistNameText;

    public ArtistsListAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Artist artist = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_artist, parent, false);

            _albumImageView = (ImageView) convertView.findViewById(R.id.imageAlbum);
            _artistNameText = (TextView) convertView.findViewById(R.id.textArtistName);
        }

        if (artist.images.size() > 2)
            Picasso.with(convertView.getContext()).load(artist.images.get(2).url).into(_albumImageView);  // 200 x 200
        else
            Picasso.with(convertView.getContext()).load(android.R.drawable.ic_menu_gallery).into(_albumImageView);

        _artistNameText.setText(artist.name);

        return convertView;
    }
}
