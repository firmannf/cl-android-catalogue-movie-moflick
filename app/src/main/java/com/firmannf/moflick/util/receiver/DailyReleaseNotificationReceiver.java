package com.firmannf.moflick.util.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firmannf.moflick.BuildConfig;
import com.firmannf.moflick.R;
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.data.MovieResultModel;
import com.firmannf.moflick.screen.main.MainActivity;
import com.firmannf.moflick.util.AppConstant;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

/**
 * Created by codelabs on 31/08/18.
 */

public class DailyReleaseNotificationReceiver extends BroadcastReceiver {

    private final static String NOTIF_ID_STRING = "Daily Reminder Notification";
    private final static int NOTIF_ID_INT = 101;
    private final static int NOTIF_REQUEST_CODE = 101;

    public DailyReleaseNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getNowPlayingMovies(context);
    }

    public void setNotification(Context context) {
        Intent intent = new Intent(context, DailyReleaseNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                NOTIF_ID_INT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }

    public void sendNotification(Context context, MovieModel movie) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIF_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String contentText = movie.getTitle() + " " + context.getString(R.string.text_notification_release_today);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NOTIF_ID_STRING)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(contentText)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000});

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIF_ID_INT, notification.build());
        }
    }

    public void getNowPlayingMovies(final Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieModel> movies = new ArrayList<>();
        String url = AppConstant.MOVIE_NOW_PLAYING_URL + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY_DEBUG;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonResult = new String(responseBody);
                MovieResultModel moviesJson = new Gson().fromJson(jsonResult, MovieResultModel.class);
                movies.addAll(moviesJson.getResults());
                MovieModel movie = movies.get(0);

                sendNotification(context, movie);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });
    }
}
