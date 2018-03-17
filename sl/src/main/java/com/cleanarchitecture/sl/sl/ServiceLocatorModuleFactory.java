package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.common.utils.StringUtils;

/**
 * Created by Shishkin on 05.03.2018.
 */

public class ServiceLocatorModuleFactory implements ModuleFactory {

    private static final String NAME = ServiceLocatorModuleFactory.class.getName();

    public ServiceLocatorModuleFactory() {
    }

    public synchronized <T extends Module> T create(final String name) {

        if (StringUtils.isNullOrEmpty(name)) return null;

        try {
            if (name.equals(ErrorModule.NAME)) {
                return (T) ErrorModule.getInstance();
            } else if (name.equals(ApplicationModule.NAME)) {
                return (T) ApplicationModule.getInstance();
            } else if (name.equals(MailUnion.NAME)) {
                return (T) MailUnion.getInstance();
            } else {
                return (T) Class.forName(name).newInstance();
            }
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
        }
        return null;
    }
}
