package com.firmannf.moflick.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by codelabs on 23/07/18.
 */

public class AppPreference {

    private SharedPreferences prefs;

    public AppPreference(Context context) {
        prefs = context.getSharedPreferences(AppConstant.KEY_APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void setFirstRun(Boolean isFirstRun) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(AppConstant.KEY_APP_PREFERENCE_FIRST_RUN, isFirstRun);
        editor.apply();
    }

    public Boolean getFirstRun() {
        return prefs.getBoolean(AppConstant.KEY_APP_PREFERENCE_FIRST_RUN, true);
    }
}
