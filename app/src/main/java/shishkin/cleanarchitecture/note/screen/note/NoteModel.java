package shishkin.cleanarchitecture.note.screen.note;

import com.cleanarchitecture.sl.model.AbsModel;


import shishkin.cleanarchitecture.note.screen.notes.NotesFragment;
import shishkin.cleanarchitecture.note.screen.notes.NotesRouter;
import shishkin.cleanarchitecture.note.screen.notes.NotesView;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteModel extends AbsModel {

    public NoteModel(NoteFragment fragment) {
        super(fragment);
    }

    @Override
    public NoteView getView() {
        return super.getView();
    }

}
