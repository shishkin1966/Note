package com.cleanarchitecture.sl.repository;

import com.cleanarchitecture.sl.data.Result;
import com.cleanarchitecture.sl.sl.Validated;

/**
 * Created by Shishkin on 27.12.2017.
 */

public abstract class AbsProvider implements Provider, Validated {

    public abstract Result<Boolean> validateExt();

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

}
