package com.firmannf.moflick.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by codelabs on 03/09/18.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new DailyReminderNotificationReceiver().setNotification(context);
        new DailyReleaseNotificationReceiver().setNotification(context);
    }
}
