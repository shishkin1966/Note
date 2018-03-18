package shishkin.cleanarchitecture.note.request;

import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class UpdateNoteRequest extends AbsRequest {

    private Note mNote;

    public UpdateNoteRequest(Note note) {
        mNote = note;
    }

    @Override
    public String getName() {
        return UpdateNoteRequest.class.getName();
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
        if (mNote == null) return;

        try {
            final NotesDb db = SLUtil.getDb();
            db.beginTransaction();
            db.NoteDao().update(mNote);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            ErrorModule.getInstance().onError(getName(), e);
        }
    }
}
