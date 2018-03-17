package shishkin.cleanarchitecture.note.request;

import com.cleanarchitecture.sl.repository.IDbProvider;
import com.cleanarchitecture.sl.request.AbsRequest;
import com.cleanarchitecture.sl.sl.SLUtil;


import shishkin.cleanarchitecture.note.ApplicationController;
import shishkin.cleanarchitecture.note.db.NotesDb;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class DbRestoreRequest extends AbsRequest {

    public static final String NAME = DbRestoreRequest.class.getName();

    public DbRestoreRequest() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public void run() {
        final IDbProvider provider = SLUtil.getDbProvider();
        if (provider != null) {
            provider.restore(NotesDb.NAME, ApplicationController.getInstance().getExternalDataPath());
        }
    }
}
