package shishkin.cleanarchitecture.note.screen.notes;

import com.cleanarchitecture.sl.model.AbsModel;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesModel extends AbsModel {

    public NotesModel(NotesFragment fragment) {
        super(fragment);

        setRouter(new NotesRouter(this));
        setPresenter(new NotesPresenter(this));
    }

    @Override
    public NotesFragment getView() {
        return super.getView();
    }

    @Override
    public NotesRouter getRouter() {
        return super.getRouter();
    }
}
