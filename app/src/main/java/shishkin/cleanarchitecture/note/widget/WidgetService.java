package shishkin.cleanarchitecture.note.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Shishkin on 19.03.2018.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(intent);
    }
}
