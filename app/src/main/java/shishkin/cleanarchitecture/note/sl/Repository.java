package shishkin.cleanarchitecture.note.sl;

import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.request.DbBackupRequest;
import shishkin.cleanarchitecture.note.request.DbRestoreRequest;

/**
 * Created by Shishkin on 16.12.2017.
 */

public class Repository {

    private Repository() {
    }

    public static void backupDb() {
        SLUtil.getRequestModule().request(SLUtil.getDbProvider(), new DbBackupRequest());
    }

    public static void restoreDb() {
        SLUtil.getRequestModule().request(SLUtil.getDbProvider(), new DbRestoreRequest());
    }

}
