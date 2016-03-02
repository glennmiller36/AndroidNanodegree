package com.fluidminds.android.studiosity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluidminds.android.studiosity.R;

/**
 * A fragment to display Card Decks for the requested Subject.
 */
public class DeckListFragment extends Fragment {

    public DeckListFragment() {
        // Required empty public constructor
    }

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        // If we have a saved state then we can restore it now
        if (savedInstanceState != null) {
            // orientation change
            //mCounter = savedInstanceState.getInt(STATE_COUNTER, 0);
        }
        else {
            //getLoaderManager().initLoader(0, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }
}
