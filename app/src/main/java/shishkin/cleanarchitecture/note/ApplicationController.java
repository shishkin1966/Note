package shishkin.cleanarchitecture.note;

import android.os.Environment;

import com.cleanarchitecture.sl.sl.ApplicationModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.io.File;


import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationModule {

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


}
