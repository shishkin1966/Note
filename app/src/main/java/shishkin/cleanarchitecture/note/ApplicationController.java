package shishkin.cleanarchitecture.note;

import android.os.Environment;

import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.io.File;


import shishkin.cleanarchitecture.note.db.NotesDb;
import shishkin.cleanarchitecture.note.request.SetSessionNotesRequest;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationModule {

    public static final String ACTION_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_CLICK";
    public static final String ACTION_LIST_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_LIST_CLICK";

    @Override
    public void onStart() {
        super.onStart();

        SLUtil.getDbProvider().getDb(NotesDb.class, NotesDb.NAME);

        Session.instantiate();

        SLUtil.getRequestModule().request(this, new SetSessionNotesRequest());
    }

    @Override
    public String getExternalDataPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + BuildConfig.APPLICATION_ID + File.separator;
    }

}
