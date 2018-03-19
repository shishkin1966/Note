package shishkin.cleanarchitecture.note.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.ApplicationController;
import shishkin.cleanarchitecture.note.request.WidgetUpdateRequest;
import shishkin.cleanarchitecture.note.screen.main.MainActivity;

/**
 * Created by Shishkin on 16.03.2018.
 */

public class ApplicationWidget extends AppWidgetProvider {

    public static final String ITEM_POSITION = "item_position";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        SLUtil.getRequestModule().request(this, new WidgetUpdateRequest(appWidgetManager, appWidgetIds));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equalsIgnoreCase(ApplicationController.ACTION_LIST_CLICK)) {
            if (!SLUtil.getActivityUnion().hasSubscribers()) {
                final Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setAction(ApplicationController.ACTION_CLICK);
                ApplicationController.getInstance().startActivity(i);
            } else {
                final Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.setAction(ApplicationController.ACTION_CLICK);
                try {
                    final PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    pi.send(ApplicationController.getInstance(), 0, i);
                } catch (Exception e) {
                    ErrorModule.getInstance().onError(getClass().getName(), e);
                }
            }
        }
    }
}
