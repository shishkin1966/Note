package shishkin.cleanarchitecture.note.request;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.cleanarchitecture.sl.request.AbsRequest;


import shishkin.cleanarchitecture.note.ApplicationController;
import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.screen.main.MainActivity;


/**
 * Created by Shishkin on 16.03.2018.
 */

public class WidgetUpdateRequest extends AbsRequest {

    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetIds;

    public WidgetUpdateRequest(AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
        if (mAppWidgetManager == null) return;

        final Context context = ApplicationController.getInstance();
        if (context == null) return;

        for (int i = 0; i < mAppWidgetIds.length; i++) {
            int appWidgetId = mAppWidgetIds[i];

            final Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setAction(ApplicationController.ACTION_CLICK);
            final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteView.setOnClickPendingIntent(R.id.ll, pendingIntent);

            mAppWidgetManager.updateAppWidget(appWidgetId, remoteView);
        }
    }
}
