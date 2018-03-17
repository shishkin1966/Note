package shishkin.cleanarchitecture.note.screen.notes;

import android.view.View;

import com.cleanarchitecture.sl.presenter.AbsPresenter;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.mail.OnDataChangeListener;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesPresenter extends AbsPresenter<NotesModel> implements View.OnClickListener, OnDataChangeListener {

    public static final String NAME = NotesPresenter.class.getName();

    public NotesPresenter(NotesModel model) {
        super(model);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                getModel().getRouter().showNote(new Note());
                break;

            default:
                getModel().getView().onClick(v);
                break;
        }
    }

    @Override
    public void onChange(String table) {
        getModel().getView().refreshData();
    }
}
