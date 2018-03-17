package shishkin.cleanarchitecture.note.sl;

import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.request.DbBackupRequest;
import shishkin.cleanarchitecture.note.request.DbRestoreRequest;
import shishkin.cleanarchitecture.note.request.InsertNoteRequest;
import shishkin.cleanarchitecture.note.request.UpdateNoteRequest;

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

    public static void insertNote(Note note) {
        SLUtil.getDbProvider().request(new InsertNoteRequest(note));
    }

    public static void updateNote(Note note) {
        SLUtil.getDbProvider().request(new UpdateNoteRequest(note));
    }

}
