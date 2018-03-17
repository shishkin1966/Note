package com.cleanarchitecture.sl.sl;

/**
 * Created by Shishkin on 05.03.2018.
 */

public interface ModuleFactory {

    <T extends Module> T create(String name);

}
