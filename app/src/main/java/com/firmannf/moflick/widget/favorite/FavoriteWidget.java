package com.firmannf.moflick.widget.favorite;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.firmannf.moflick.R;
import com.firmannf.moflick.widget.StackWidgetService;

import static com.firmannf.moflick.util.AppConstant.ACTION_TOAST;
import static com.firmannf.moflick.util.AppConstant.EXTRAS_TITLE;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.favoritewidget_stackview_movie, intent);
        views.setEmptyView(R.id.favoritewidget_stackview_movie, R.id.favoritewidget_textview_emptyview);

        Intent toastIntent = new Intent(context, FavoriteWidget.class);
        toastIntent.setAction(ACTION_TOAST);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.favoritewidget_stackview_movie, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_TOAST)) {
            String movieTitle = intent.getStringExtra(EXTRAS_TITLE);
            Toast.makeText(context, "Movie Title : " + movieTitle, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
}
