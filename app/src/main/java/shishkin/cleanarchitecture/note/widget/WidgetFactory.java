package shishkin.cleanarchitecture.note.widget;

import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.cleanarchitecture.common.utils.StringUtils;
import com.google.gson.Gson;


import java.util.List;


import shishkin.cleanarchitecture.note.ApplicationController;
import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.Session;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.data.NoteItem;
import shishkin.cleanarchitecture.note.data.NoteJson;

/**
 * Created by Shishkin on 19.03.2018.
 */

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Note> mData;

    public WidgetFactory(Intent intent) {
        getData();
    }

    private void getData() {
        mData = Session.getInstance().getNotes();
    }

    @Override
    public void onCreate() {
        getData();
    }

    @Override
    public void onDataSetChanged() {
        getData();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews view = new RemoteViews(ApplicationController.getInstance().getPackageName(),
                R.layout.widget_item);

        if (mData == null) return view;

        final NoteJson json = new Gson().fromJson(mData.get(position).getNote(), NoteJson.class);
        view.setTextViewText(R.id.note, "");
        view.setViewVisibility(R.id.note, View.GONE);

        view.setTextViewText(R.id.title, "");
        view.setViewVisibility(R.id.title, View.GONE);

        view.setTextViewText(R.id.title, json.getTitle());
        if (!StringUtils.isNullOrEmpty(json.getTitle())) {
            view.setViewVisibility(R.id.title, View.VISIBLE);
        }

        final List<NoteItem> items = json.getItems();
        if (items != null && !items.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            int row = 1;
            for (int i = 0; i < items.size(); i++) {
                if (!StringUtils.isNullOrEmpty(StringUtils.allTrim(items.get(i).getTitle()))) {
                    if (row > 1) {
                        sb.append("\n");
                    }
                    sb.append("" + (row) + ". " + items.get(i).getTitle());
                    row++;
                }
            }
            view.setTextViewText(R.id.note, sb.toString());
            if (!StringUtils.isNullOrEmpty(sb.toString())) {
                view.setViewVisibility(R.id.note, View.VISIBLE);
            }
        }

        final Intent intent = new Intent();
        intent.putExtra(ApplicationWidget.ITEM_POSITION, position);
        view.setOnClickFillInIntent(R.id.ll, intent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
