package com.firmannf.moflick.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by codelabs on 15/08/18.
 */

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
