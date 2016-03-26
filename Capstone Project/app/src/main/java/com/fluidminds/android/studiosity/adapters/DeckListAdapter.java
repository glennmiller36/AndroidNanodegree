package com.fluidminds.android.studiosity.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.models.DeckModel;

import java.util.ArrayList;

/**
 * DeckListAdapter exposes a list of subject flash cards
 * from a {@link Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {
    private ArrayList<DeckModel> mDataSet = new ArrayList<>();
    private static MyClickListener mItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;
        TextView mSubtitle;
        LinearLayout mButtonStudy;
        LinearLayout mButtonStats;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.textName);
            mSubtitle = (TextView) itemView.findViewById(R.id.textSubtitle);
            mButtonStudy = (LinearLayout) itemView.findViewById(R.id.buttonStudy);
            mButtonStats = (LinearLayout) itemView.findViewById(R.id.buttonStats);

            itemView.setOnClickListener(this);
            mButtonStudy.setOnClickListener(this);
            mButtonStats.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean glenn = false;
            if (v.getId() == mButtonStudy.getId()) {
                mItemClickListener.onStudyClick(getPosition(), v);
            }
            else if (v.getId() == mButtonStats.getId()) {
                mItemClickListener.onStatsClick(getPosition(), v);
            }
            else
                mItemClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.mItemClickListener = myClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_deck, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mDataSet.get(position).getName());
        holder.mSubtitle.setText(mDataSet.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapData(ArrayList<DeckModel> dataset) {
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    /**
     * Return strongly typed business object for the cursor.
     */
    public DeckModel get(int position) {
        return mDataSet.get(position);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onStudyClick(int position, View v);
        public void onStatsClick(int position, View v);
    }
}