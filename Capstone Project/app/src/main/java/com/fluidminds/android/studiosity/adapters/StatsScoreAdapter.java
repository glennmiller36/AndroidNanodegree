package com.fluidminds.android.studiosity.adapters;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.app.StudiosityApp;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.utils.Converters;
import com.fluidminds.android.studiosity.utils.ThemeColor;

import java.util.ArrayList;

/**
 * StatsScoreAdapter exposes a list of scores per flash card
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class StatsScoreAdapter extends RecyclerView.Adapter<StatsScoreAdapter.ViewHolder> {
    private ArrayList<CardModel> mDataSet = new ArrayList<>();
    private static MyClickListener mItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout mAccuracy;
        TextView mPercentComplete;
        TextView mPercentSymbol;
        TextView mQuestion;

        public ViewHolder(View itemView) {
            super(itemView);
            mAccuracy = (LinearLayout) itemView.findViewById(R.id.linearAccuracy);
            mPercentComplete = (TextView) itemView.findViewById(R.id.textPercentComplete);
            mPercentSymbol = (TextView) itemView.findViewById(R.id.textPercentSymbol);
            mQuestion = (TextView) itemView.findViewById(R.id.textQuestion);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.mItemClickListener = myClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stats_score, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int percentAccuracy = mDataSet.get(position).getPercentCorrect();

        int backgroundColor = Converters.accuracyPercentToColor(percentAccuracy);
        holder.mAccuracy.setBackgroundColor(backgroundColor);
        holder.mPercentComplete.setText(String.valueOf(percentAccuracy));
        holder.mQuestion.setText(mDataSet.get(position).getQuestion());

        int textColor = Color.WHITE;
        if (!ThemeColor.isWhiteContrastColor(backgroundColor))
            textColor = ContextCompat.getColor(StudiosityApp.getInstance(), R.color.textColorPrimary);

        holder.mPercentComplete.setTextColor(textColor);
        holder.mPercentSymbol.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapData(ArrayList<CardModel> dataset) {
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    /**
     * Return strongly typed business object for the cursor.
     */
    public CardModel get(int position) {
        return mDataSet.get(position);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}