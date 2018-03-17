package shishkin.cleanarchitecture.note.screen.notes;

import com.cleanarchitecture.sl.model.AbsModel;


import shishkin.cleanarchitecture.note.model.BaseModelRouter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class NotesRouter extends BaseModelRouter {

    public NotesRouter(AbsModel model) {
        super(model);
    }

    public void showSetting() {
        //showFragment(SettingFragment.newInstance());
    }

    public void showCurrency() {
        //showFragment(CurrencyFragment.newInstance());
    }

    public void backupDb() {
        //Repository.backupDb();
    }

    public void restoreDb() {
        //Repository.restoreDb();
    }

}
