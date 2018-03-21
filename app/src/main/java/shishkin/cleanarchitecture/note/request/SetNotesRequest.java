package shishkin.cleanarchitecture.note.request;

import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.List;


import shishkin.cleanarchitecture.note.Session;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class SetNotesRequest extends AbsRequest {

    public static final String NAME = SetNotesRequest.class.getName();

    private List<Note> mItems;

    public SetNotesRequest(List<Note> items) {
        mItems = items;
    }

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
        if (mItems == null) return;

        try {
            final NotesDb db = SLUtil.getDb();
            for (Note note : mItems) {
                db.NoteDao().update(note);
            }

            Session.getInstance().onChangeNotes();
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
        }
    }
}
