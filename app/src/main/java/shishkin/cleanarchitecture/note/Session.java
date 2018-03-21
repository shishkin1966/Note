package shishkin.cleanarchitecture.note;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.db.NotesDb;
import shishkin.cleanarchitecture.note.widget.ApplicationWidget;

public class Session {

    private static volatile Session sInstance;
    private List<Note> mNotes = new ArrayList<>();

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (Session.class) {
                if (sInstance == null) {
                    sInstance = new Session();
                }
            }
        }
    }

    public static Session getInstance() {
        if (sInstance == null) {
            synchronized (Session.class) {
                if (sInstance == null) {
                    sInstance = new Session();
                }
            }
        }
        return sInstance;
    }

    private Session() {
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

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ApplicationController.getInstance());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(ApplicationController.getInstance(), ApplicationWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list);
    }

}
