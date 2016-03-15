package com.fluidminds.android.studiosity.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.fragments.SubjectListFragment;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.ThemeColor;
import com.fluidminds.android.studiosity.views.SquareView;

/**
 * SubjectListAdapter exposes a list of school subjects
 * from a {@link Cursor} to a {@link android.widget.GridView}.
 */
public class SubjectListAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 1;

    /**
     * Cache of the children views for a subject grid item.
     */
    public static class ViewHolder {
        public final SquareView mContainer;
        public final TextView mName;

        public ViewHolder(View view) {
            mContainer = (SquareView) view.findViewById(R.id.container);
            mName = (TextView) view.findViewById(R.id.textName);
        }
    }

    public SubjectListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_subject, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Read data from cursor
        Integer color = cursor.getInt(SubjectListFragment.COL_COLOR);
        viewHolder.mContainer.setBackgroundColor(color);
        viewHolder.mName.setText(cursor.getString(SubjectListFragment.COL_NAME));

        if (ThemeColor.isWhiteContrastColor(color))
            viewHolder.mName.setTextColor(Color.WHITE);
        else
            viewHolder.mName.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Return strongly typed business object for the cursor.
     */
    public SubjectModel get(int position) {
        Cursor cursor = getCursor();

        SubjectModel model = new SubjectModel(
            cursor.getLong(SubjectListFragment.COL_ID),
            cursor.getString(SubjectListFragment.COL_NAME),
            cursor.getInt(SubjectListFragment.COL_COLOR)
        );

        return model;
    }
}
