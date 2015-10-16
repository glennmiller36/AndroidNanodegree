package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * RemoteViewsService controlling the data being shown in the scrollable football scores widget
 */
public class ScoresWidgetRemoteViewsService extends RemoteViewsService {

    private Cursor data = null;

    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.SCORES_TABLE + "." + DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    private static final int ID_INDEX = 0;
    private static final int HOME_INDEX = 1;
    private static final int HOME_GOALS_INDEX =2;
    private static final int AWAY_INDEX = 3;
    private static final int AWAY_GOALS_INDEX = 4;
    private static final int TIME_INDEX = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                Uri todaysScoresUri = DatabaseContract.scores_table.buildScoreWithDate();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                data = getContentResolver().query(todaysScoresUri,
                        SCORE_COLUMNS,
                        null,
                        new String[]{dateFormatter.format(calendar.getTime())},
                        DatabaseContract.scores_table.DATE_COL + " ASC");
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.widget_scores_list_item);

                String homeTeam = data.getString(HOME_INDEX);
                Integer homeGoals = data.getInt(HOME_GOALS_INDEX);
                String awayTeam = data.getString(AWAY_INDEX);
                Integer awayGoals = data.getInt(AWAY_GOALS_INDEX);
                String matchTime = data.getString(TIME_INDEX);

                remoteViews.setTextViewText(R.id.home_name, homeTeam);
                remoteViews.setTextViewText(R.id.away_name, awayTeam);

                if (homeGoals > -1 && awayGoals > -1)
                    remoteViews.setTextViewText(R.id.score_textview, homeGoals.toString() + " - " + awayGoals.toString());
                else
                    remoteViews.setTextViewText(R.id.score_textview, matchTime);

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(ID_INDEX);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}