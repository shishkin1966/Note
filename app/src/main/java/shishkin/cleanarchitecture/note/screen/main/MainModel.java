package shishkin.cleanarchitecture.note.screen.main;

import com.cleanarchitecture.sl.model.AbsModel;


import shishkin.cleanarchitecture.note.model.BaseModelRouter;

/**
 * Created by Shishkin on 29.11.2017.
 */

public class MainModel extends AbsModel {

    public MainModel(MainActivity activity) {
        super(activity);

        setRouter(new BaseModelRouter(this));
    }

    @Override
    public BaseModelRouter getRouter() {
        return super.getRouter();
    }
}
