package com.fluidminds.android.studiosity.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.models.CardModel;

import java.util.ArrayList;

/**
 * StudyListAdapter exposes a list of subject flash cards
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class StudyListAdapter extends CardListAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_study, parent, false);

        return new ViewHolder(view);
    }
}