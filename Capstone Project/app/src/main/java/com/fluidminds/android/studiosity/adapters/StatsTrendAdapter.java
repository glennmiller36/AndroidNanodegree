package com.fluidminds.android.studiosity.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.models.QuizModel;

import java.util.ArrayList;

/**
 * StatsTrendAdapter exposes a list of quiz statistics
 * from a {@link Cursor} to a {@link RecyclerView}.
 */
public class StatsTrendAdapter extends RecyclerView.Adapter<StatsTrendAdapter.ViewHolder> {
    private ArrayList<QuizModel> mDataSet = new ArrayList<>();
    private static MyClickListener mItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TextView mQuestion;
        //TextView mAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            //mQuestion = (TextView) itemView.findViewById(R.id.textQuestion);
            //mAnswer = (TextView) itemView.findViewById(R.id.textAnswer);
            //itemView.setOnClickListener(this);
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
                .inflate(R.layout.list_item_trend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.mQuestion.setText(mDataSet.get(position).getQuestion());
        //holder.mAnswer.setText(mDataSet.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapData(ArrayList<QuizModel> dataset) {
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    /**
     * Return strongly typed business object for the cursor.
     */
    public QuizModel get(int position) {
        return mDataSet.get(position);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}