package shishkin.cleanarchitecture.note.request;

import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.Session;
import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class SetSessionNotesRequest extends AbsRequest {

    public static final String NAME = SetSessionNotesRequest.class.getName();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
        try {
            final NotesDb db = SLUtil.getDb();
            Session.getInstance().setNotes(db.NoteDao().get());
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
        }
    }
}
