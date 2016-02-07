package com.fluidminds.android.studiosity.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.fragments.SubjectListFragment;
import com.fluidminds.android.studiosity.models.SubjectModel;

/**
 * SubjectAdapter exposes a list of school subjects
 * from a {@link Cursor} to a {@link android.widget.GridView}.
 */
public class SubjectAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    /**
     * Cache of the children views for a subject grid item.
     */
    public static class ViewHolder {
        public final TextView textSubject;

        public ViewHolder(View view) {
            textSubject = (TextView) view.findViewById(R.id.textSubject);
        }
    }

    public SubjectAdapter(Context context, Cursor c, int flags) {
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
        viewHolder.textSubject.setText(cursor.getString(SubjectListFragment.COL_SUBJECT));
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public SubjectModel get(int position) {
        Cursor cursor = getCursor();

        SubjectModel model = new SubjectModel();
        model.setId(cursor.getLong(SubjectListFragment.COL_ID));
        model.setSubject(cursor.getString(SubjectListFragment.COL_SUBJECT));
        model.setColorInt(cursor.getInt(SubjectListFragment.COL_COLOR));
        model.MarkAsOld();

        return model;
    }
}
