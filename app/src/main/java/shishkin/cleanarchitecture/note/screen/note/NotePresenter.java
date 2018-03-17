package shishkin.cleanarchitecture.note.screen.note;

import com.cleanarchitecture.sl.presenter.AbsPresenter;


import shishkin.cleanarchitecture.note.data.Note;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotePresenter extends AbsPresenter<NoteModel> {

    public NotePresenter(NoteModel model) {
        super(model);
    }

    public void onBackPressed(Note note, String operation) {
        if (operation.equals(NoteFragment.OPERATION_INSERT)) {
            note.setCreated(System.currentTimeMillis());
            getModel().getInteractor().insert(note);
        } else {
            note.setModified(System.currentTimeMillis());
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
