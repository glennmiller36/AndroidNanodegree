package com.fluidminds.android.studiosity.adapters;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.app.StudiosityApp;
import com.fluidminds.android.studiosity.models.QuizModel;
import com.fluidminds.android.studiosity.utils.Converters;
import com.fluidminds.android.studiosity.utils.ThemeColor;

import java.util.ArrayList;

/**
 * StatsTrendAdapter exposes a list of quiz statistics
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class StatsTrendAdapter extends RecyclerView.Adapter<StatsTrendAdapter.ViewHolder> {
    private ArrayList<QuizModel> mDataSet = new ArrayList<>();
    private static MyClickListener mItemClickListener;
    private int mCurrentIndex = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar mProgressBar;
        RelativeLayout mIndexCircle;
        TextView mIndexText;
        ImageView mActiveArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            mIndexCircle = (RelativeLayout) itemView.findViewById(R.id.circleIndex);
            mIndexText = (TextView) itemView.findViewById(R.id.textIndex);
            mActiveArrow = (ImageView) itemView.findViewById(R.id.imageActiveArrow);

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
                .inflate(R.layout.list_item_stats_trend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizModel model = mDataSet.get(position);

        int accuracyColor = Converters.accuracyPercentToColor(model.getPercentCorrect());
        if (accuracyColor == ThemeColor.RED)
            holder.mProgressBar.setProgressDrawable(StudiosityApp.getInstance().getResources().getDrawable(R.drawable.progress_drawable_red));
        else if (accuracyColor == ThemeColor.YELLOW)
            holder.mProgressBar.setProgressDrawable(StudiosityApp.getInstance().getResources().getDrawable(R.drawable.progress_drawable_yellow));
        else
            holder.mProgressBar.setProgressDrawable(StudiosityApp.getInstance().getResources().getDrawable(R.drawable.progress_drawable_green));

        holder.mProgressBar.setProgress(model.getPercentCorrect());
        holder.mIndexText.setText(String.valueOf(position + 1));

        if (mCurrentIndex == position) {
            holder.mIndexCircle.setBackgroundResource(R.drawable.trend_circle_black);
            holder.mIndexText.setTextColor(ContextCompat.getColor(StudiosityApp.getInstance(), android.R.color.white));;
            holder.mActiveArrow.setVisibility(View.VISIBLE);
        }
        else {
            holder.mIndexCircle.setBackground(null);
            holder.mIndexText.setTextColor(ContextCompat.getColor(StudiosityApp.getInstance(), R.color.textColorPrimary));;
            holder.mActiveArrow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapData(ArrayList<QuizModel> dataset) {
        mDataSet = dataset;

        // default to the most recent Quiz
        if (dataset != null && !dataset.isEmpty())
            mCurrentIndex = mDataSet.size() - 1;

        notifyDataSetChanged();
    }

    /**
     * Return strongly typed business object for the cursor.
     */
    public QuizModel get(int position) {
        return mDataSet.get(position);
    }

    public void setActive(int position) {
        mCurrentIndex = position;
        notifyDataSetChanged();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}