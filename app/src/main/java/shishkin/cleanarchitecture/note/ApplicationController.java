package shishkin.cleanarchitecture.note;

import com.cleanarchitecture.sl.sl.ApplicationModule;

/**
 * Created by Shishkin on 08.02.2018.
 */

public class ApplicationController extends ApplicationModule {

    @Override
    public void onStart() {
        super.onStart();

        //SLUtil.getDbProvider().getDb(CleanArchitectureDb.class, CleanArchitectureDb.NAME);
    }
}
