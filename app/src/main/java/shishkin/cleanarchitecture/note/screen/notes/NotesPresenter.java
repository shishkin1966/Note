package shishkin.cleanarchitecture.note.screen.notes;

import android.view.View;

import com.cleanarchitecture.common.utils.SafeUtils;
import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.event.ui.ShowErrorMessageEvent;
import com.cleanarchitecture.sl.presenter.AbsPresenter;
import com.cleanarchitecture.sl.request.ResponseListener;
import com.cleanarchitecture.sl.sl.SLUtil;


import java.util.List;


import shishkin.cleanarchitecture.note.R;
import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.request.GetNotesRequest;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesPresenter extends AbsPresenter<NotesModel> implements View.OnClickListener, ResponseListener {

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
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                final Note note = new Note();
                note.setPoradok(getModel().getView().getItems().size());
                getModel().getRouter().showNote(note);
                break;

            default:
                getModel().getView().onClick(v);
                break;
        }
    }

    @Override
    public void onStart() {
        getModel().getView().showProgressBar();
        getModel().getInteractor().getNotes(NAME);
    }

    @Override
    public void response(Result result) {
        getModel().getView().hideProgressBar();
        if (result.hasError()) {
            SLUtil.getActivityUnion().showErrorMessage(new ShowErrorMessageEvent(result));
        } else {
            if (result.getName().equals(GetNotesRequest.NAME)) {
                getModel().getView().setItems(SafeUtils.cast(result.getData()));
            }
        }
    }

    public void onNotesChanged() {
        final List<Note> items = getModel().getView().getItems();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setPoradok(i);
        }
        getModel().getInteractor().updateNotes(items);
    }


}
