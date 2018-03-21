package shishkin.cleanarchitecture.note.request;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.ApplicationController;
import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.Session;
import shishkin.cleanarchitecture.note.db.NotesDb;
import shishkin.cleanarchitecture.note.screen.main.MainActivity;
import shishkin.cleanarchitecture.note.widget.ApplicationWidget;
import shishkin.cleanarchitecture.note.widget.WidgetService;


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

        final NotesDb db = SLUtil.getDb();
        Session.getInstance().setNotes(db.NoteDao().get());

        for (int i = 0; i < mAppWidgetIds.length; i++) {
            int appWidgetId = mAppWidgetIds[i];
            final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            setClick(context, remoteView);

            setList(context, remoteView, appWidgetId);

            setListClick(context, remoteView, appWidgetId);

            mAppWidgetManager.updateAppWidget(appWidgetId, remoteView);
        }
    }

    private void setClick(Context context, RemoteViews remoteView) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(ApplicationController.ACTION_CLICK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteView.setOnClickPendingIntent(R.id.ll, pendingIntent);
    }


    private void setList(Context context, RemoteViews remoteView, int appWidgetId) {
        final Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteView.setRemoteAdapter(R.id.list, adapter);
    }

    private void setListClick(Context context, RemoteViews remoteView, int appWidgetId) {
        final Intent intent = new Intent(context, ApplicationWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setAction(ApplicationController.ACTION_LIST_CLICK);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setPendingIntentTemplate(R.id.list, pendingIntent);
    }

}
