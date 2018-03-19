package shishkin.cleanarchitecture.note;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Environment;

import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.db.NotesDb;
import shishkin.cleanarchitecture.note.widget.ApplicationWidget;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationModule {

    public static final String ACTION_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_CLICK";
    public static final String ACTION_LIST_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_LIST_CLICK";

    private List<Note> mNotes = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();

        SLUtil.getDbProvider().getDb(NotesDb.class, NotesDb.NAME);
    }

    @Override
    public String getExternalDataPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + BuildConfig.APPLICATION_ID + File.separator;
    }

    public List<Note> getNotes() {
        return mNotes;
    }

    public void setNotes(List<Note> list) {
        mNotes = list;
    }

    public void onChangeNotes() {
        final NotesDb db = SLUtil.getDb();
        mNotes = db.NoteDao().get();

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, ApplicationWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list);
    }

}
