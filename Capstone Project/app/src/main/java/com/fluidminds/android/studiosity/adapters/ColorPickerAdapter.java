package com.fluidminds.android.studiosity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        public GradientDrawable mCircleDrawable;
        public ImageView mActiveCheckmark;
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
            LinearLayout circle = (LinearLayout)row.findViewById(R.id.colorCircle);

            // forward click to parent GridView
            circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                ((GridView) aparent).performItemClick(v, aposition, 0); // Let the event be handled in onItemClick()
                }
            });

            viewHolder.mCircleDrawable = (GradientDrawable)circle.getBackground();
            viewHolder.mActiveCheckmark = (ImageView)row.findViewById(R.id.activeCheckmark);

            row.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)row.getTag();
        }

        int color = m500Colors[position];

        viewHolder.mCircleDrawable.setColor(color);

        if (mSelectedColor == color) {
            // determine the appropriate checkmark color to show that contrasts the color
            if (ThemeColor.isWhiteContrastColor(color)) {
                viewHolder.mActiveCheckmark.setImageResource(R.drawable.ic_check_white_24dp);
            } else {
                viewHolder.mActiveCheckmark.setImageResource(R.drawable.ic_check_black_24dp);
            }
        }
        else
            viewHolder.mActiveCheckmark.setImageResource(0);

        return row;
    }
}