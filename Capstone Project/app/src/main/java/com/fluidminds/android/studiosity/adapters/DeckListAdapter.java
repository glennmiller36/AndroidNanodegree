package com.fluidminds.android.studiosity.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private static MyClickListener myClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.textName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
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
    }

    public void addItem(DeckModel dataObj, int index) {
        mDataSet.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapData(ArrayList<DeckModel> dataset) {
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}