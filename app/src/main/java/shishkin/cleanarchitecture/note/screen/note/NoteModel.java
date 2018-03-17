package shishkin.cleanarchitecture.note.screen.note;

import com.cleanarchitecture.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NoteModel extends AbsModel {

    public NoteModel(NoteFragment fragment) {
        super(fragment);

        setInteractor(new NoteInteractor());
        setPresenter(new NotePresenter(this));
    }

    @Override
    public NoteView getView() {
        return super.getView();
    }


    @Override
    public NoteInteractor getInteractor() {
        return super.getInteractor();
    }

    @Override
    public NotePresenter getPresenter() {
        return super.getPresenter();
    }

}
