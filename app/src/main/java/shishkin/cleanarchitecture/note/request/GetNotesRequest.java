package shishkin.cleanarchitecture.note.request;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.request.AbsDataResultMailRequest;
import com.cleanarchitecture.sl.sl.ErrorModule;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.List;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class GetNotesRequest extends AbsDataResultMailRequest<List<Note>> {

    public static final String NAME = GetNotesRequest.class.getName();

    public GetNotesRequest(String responseListener) {
        super(responseListener);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public Result<List<Note>> getData() {
        try {
            final NotesDb db = SLUtil.getDb();
            return new Result<>(db.NoteDao().get());
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
            return new Result<>().setError(NAME, e);
        }
    }
}
