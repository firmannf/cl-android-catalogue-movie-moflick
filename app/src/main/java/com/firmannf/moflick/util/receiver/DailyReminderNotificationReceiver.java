package com.firmannf.moflick.util.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firmannf.moflick.R;
import com.firmannf.moflick.screen.main.MainActivity;

import java.util.Calendar;

/**
 * Created by codelabs on 31/08/18.
 */

public class DailyReminderNotificationReceiver extends BroadcastReceiver {

    private final static String NOTIF_ID_STRING = "Daily Reminder Notification";
    private final static int NOTIF_ID_INT = 100;
    private final static int NOTIF_REQUEST_CODE = 100;

    public DailyReminderNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context);
    }

    public void setNotification(Context context) {
        Intent intent = new Intent(context, DailyReminderNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                NOTIF_ID_INT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }

    public void sendNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIF_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NOTIF_ID_STRING)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.text_notification_daily_reminder))
                .setAutoCancel(true)
                .setVibrate(new long[]{1000});

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIF_ID_INT, notification.build());
        }
    }
}
