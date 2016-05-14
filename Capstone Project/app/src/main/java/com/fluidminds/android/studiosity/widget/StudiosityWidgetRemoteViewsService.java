package com.fluidminds.android.studiosity.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.data.DataContract;
import com.fluidminds.android.studiosity.fragments.SubjectListFragment;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.ThemeColor;

/**
 * RemoteViewsService controlling the mData being shown in the scrollable Subjects widget
 */
public class StudiosityWidgetRemoteViewsService extends RemoteViewsService {

    private Cursor mData = null;

    private static final String[] SUBJECT_COLUMNS = {
            DataContract.SubjectEntry.TABLE_NAME + "." + DataContract.SubjectEntry._ID,
            DataContract.SubjectEntry.COLUMN_NAME,
            DataContract.SubjectEntry.COLUMN_COLOR
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (mData != null) {
                    mData.close();
                }

                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // mData. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission

                // Sort order:  Ascending, by subject name.
                String sortOrder = DataContract.SubjectEntry.COLUMN_NAME + " COLLATE NOCASE ASC";

                mData = getContentResolver().query(DataContract.SubjectEntry.CONTENT_URI,
                        SUBJECT_COLUMNS,
                        null,
                        null,
                        sortOrder);
            }

            @Override
            public void onDestroy() {
                if (mData != null) {
                    mData.close();
                    mData = null;
                }
            }

            @Override
            public int getCount() {
                return mData == null ? 0 : mData.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        mData == null || !mData.moveToPosition(position)) {
                    return null;
                }

                SubjectModel model = new SubjectModel(
                        mData.getLong(SubjectListFragment.COL_ID),
                        mData.getString(SubjectListFragment.COL_NAME),
                        mData.getInt(SubjectListFragment.COL_COLOR)
                );

                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.widget_subject_list_item);

                remoteViews.setTextViewText(R.id.textName, mData.getString(SubjectListFragment.COL_NAME));
                remoteViews.setInt(R.id.textName, "setBackgroundColor", model.getColorInt());

                // Determine appropriate contrast color for the tab color
                if (!ThemeColor.isWhiteContrastColor(model.getColorInt()))
                    remoteViews.setInt(R.id.textName, "setTextColor", Color.BLACK);

                    // click on row to launch Deck List
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra("subjectmodel", model);
                remoteViews.setOnClickFillInIntent(R.id.textName, fillInIntent);

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_subject_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (mData.moveToPosition(position))
                    return mData.getLong(SubjectListFragment.COL_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}