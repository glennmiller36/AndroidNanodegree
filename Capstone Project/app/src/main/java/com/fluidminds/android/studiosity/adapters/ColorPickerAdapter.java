package com.fluidminds.android.studiosity.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.utils.ThemeColor;

/**
 * ColorPickerAdapter exposes a list of colors
 * from a Integer array to a {@link android.widget.GridView}.
 */
public class ColorPickerAdapter extends ArrayAdapter<Integer> {

    private Context mContext;
    private int mLayoutResourceId;
    private Integer m500Colors[] = null;
    private int mSelectedColor;

    /**
     * Cache of the children views for a color grid item.
     */
    public static class ViewHolder {
        public FloatingActionButton fabColor = null;
    }

    public ColorPickerAdapter(Context context, int layoutResourceId, Integer[] data, int selectedColor) {
        super(context, layoutResourceId, data);
        this.mLayoutResourceId = layoutResourceId;
        this.mContext = context;
        this.m500Colors = data;
        this.mSelectedColor = selectedColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;

        final int aposition = position;
        final ViewGroup aparent = parent;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.fabColor = (FloatingActionButton)row.findViewById(R.id.fabColor);

            // forward FAB click to parent GridView
            viewHolder.fabColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GridView) aparent).performItemClick(v, aposition, 0); // Let the event be handled in onItemClick()
                }
            });
            row.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)row.getTag();
        }

        int color = m500Colors[position];

        viewHolder.fabColor.setBackgroundTintList(ColorStateList.valueOf(color));

        if (mSelectedColor == color) {
            // determine the appropriate checkmark color to show that contrasts the color
            if (ThemeColor.isWhiteContrastColor(color)) {
                viewHolder.fabColor.setImageResource(R.drawable.ic_check_white_24dp);
            } else {
                viewHolder.fabColor.setImageResource(R.drawable.ic_check_black_24dp);
            }
        }
        else
            viewHolder.fabColor.setImageResource(0);

        return row;
    }
}