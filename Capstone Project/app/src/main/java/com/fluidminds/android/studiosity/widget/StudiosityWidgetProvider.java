package com.fluidminds.android.studiosity.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.activities.DeckListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class StudiosityWidgetProvider extends AppWidgetProvider {

    /*
     * Called whenever the widget is added to a host or by the system when the widget needs to be updated automatically.
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_subject);

            // Create an Intent to launch DeckListActivity for the selected Subject
            Intent intent = new Intent(context, DeckListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

            // Sets the remote adapter used to fill in the list items
            remoteViews.setRemoteAdapter(R.id.widget_list, new Intent(context, StudiosityWidgetRemoteViewsService.class));
            remoteViews.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}