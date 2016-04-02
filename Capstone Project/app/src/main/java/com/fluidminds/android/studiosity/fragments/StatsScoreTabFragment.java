package com.fluidminds.android.studiosity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluidminds.android.studiosity.R;

/**
 * Stats for each individual Question..
 */
public class StatsScoreTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_score_tab, container, false);
    }
}