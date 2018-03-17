package shishkin.cleanarchitecture.note.model;

import com.cleanarchitecture.sl.model.AbsModel;
import com.cleanarchitecture.sl.model.AbsModelRouter;


import shishkin.cleanarchitecture.note.screen.notes.NotesFragment;

/**
 * Created by Shishkin on 26.01.2018.
 */

public class BaseModelRouter extends AbsModelRouter {

    public BaseModelRouter(AbsModel model) {
        super(model);
    }

    @Override
    public void showMainFragment() {
        showFragment(NotesFragment.newInstance(), true);
    }

}
