package shishkin.cleanarchitecture.note;

import android.content.Context;
import android.os.Environment;
import androidx.multidex.MultiDexApplication;

import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.io.File;


import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationModule {

    public static final String ACTION_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_CLICK";
    public static final String ACTION_LIST_CLICK = BuildConfig.APPLICATION_ID + ".ACTION_LIST_CLICK";

    @Override
    public void onStartApplication() {
        super.onStartApplication();

        SLUtil.getDbProvider().getDb(NotesDb.class, NotesDb.NAME);

        Session.instantiate();
    }

    @Override
    public String getExternalDataPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + BuildConfig.APPLICATION_ID + File.separator;
    }

}
