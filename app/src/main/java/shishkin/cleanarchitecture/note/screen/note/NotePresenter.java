package shishkin.cleanarchitecture.note.screen.note;

import com.cleanarchitecture.sl.presenter.AbsPresenter;
import com.google.gson.Gson;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.data.NoteJson;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotePresenter extends AbsPresenter<NoteModel> {

    public NotePresenter(NoteModel model) {
        super(model);
    }

    public void onBackPressed(Note note, NoteJson newNoteJson, String operation) {
        if (newNoteJson == null) return;
        if (note == null) return;

        final String json = new Gson().toJson(newNoteJson);
        if (!operation.equals(NoteFragment.OPERATION_INSERT)) {
            if (!json.equals(note.getNote())) {
                note.setModified(System.currentTimeMillis());
            }
        }
        note.setNote(json);

        if (operation.equals(NoteFragment.OPERATION_INSERT)) {
            note.setCreated(System.currentTimeMillis());
            getModel().getInteractor().insert(note);
        } else {
            getModel().getInteractor().update(note);
        }
    }

    @Override
    public String getName() {
        return NotePresenter.class.getName();
    }

    @Override
    public boolean isRegister() {
        return false;
    }
}
