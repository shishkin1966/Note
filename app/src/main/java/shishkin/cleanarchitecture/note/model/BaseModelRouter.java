package shishkin.cleanarchitecture.note.model;

import com.cleanarchitecture.sl.model.AbsModel;
import com.cleanarchitecture.sl.model.AbsModelRouter;

/**
 * Created by Shishkin on 26.01.2018.
 */

public class BaseModelRouter extends AbsModelRouter {

    public BaseModelRouter(AbsModel model) {
        super(model);
    }

    @Override
    public void showMainFragment() {
    }

}
