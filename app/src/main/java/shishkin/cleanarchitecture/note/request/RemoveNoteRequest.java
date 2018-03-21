package shishkin.cleanarchitecture.note.request;

import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.ApplicationController;
import shishkin.cleanarchitecture.note.Session;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class RemoveNoteRequest extends AbsRequest {

    private Note mNote;

    public RemoveNoteRequest(Note note) {
        mNote = note;
    }

    @Override
    public String getName() {
        return RemoveNoteRequest.class.getName();
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
            db.NoteDao().delete(mNote);

            Session.getInstance().onChangeNotes();
        } catch (Exception e) {
            ErrorModule.getInstance().onError(getName(), e);
        }
    }
}
