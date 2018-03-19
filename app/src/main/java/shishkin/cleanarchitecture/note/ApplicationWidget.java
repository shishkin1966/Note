package shishkin.cleanarchitecture.note;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.request.WidgetUpdateRequest;

/**
 * Created by Shishkin on 16.03.2018.
 */

public class ApplicationWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SLUtil.getRequestModule().request(this, new WidgetUpdateRequest(appWidgetManager, appWidgetIds));
    }
}
