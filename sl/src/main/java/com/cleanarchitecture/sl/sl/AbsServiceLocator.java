package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.common.utils.StringUtils;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Абстрактный администратор
 */
@SuppressWarnings("unused")
public abstract class AbsServiceLocator implements ServiceLocator {

    private static final String NAME = AbsServiceLocator.class.getName();

    private Map<String, Module> mModules = Collections.synchronizedMap(new ConcurrentHashMap<String, Module>());

    public String getShortName(final String name) {
        return StringUtils.last(name, "\\.");
    }

    @Override
    public <C> C get(final String name) {
        if (!exists(name)) {
            if (!registerModule(name)) {
                return null;
            }
        }

        try {
            final String moduleName = getShortName(name);
            if (mModules.get(moduleName) != null) {
                return (C) mModules.get(moduleName);
            } else {
                mModules.remove(moduleName);
            }
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
        }
        return null;
    }

    @Override
    public boolean exists(final String moduleName) {
        if (StringUtils.isNullOrEmpty(moduleName)) {
            return false;
        }

        return mModules.containsKey(getShortName(moduleName));
    }

    @Override
    public boolean registerModule(final Module newModule) {
        if (newModule != null && !StringUtils.isNullOrEmpty(newModule.getName())) {
            if (mModules.containsKey(getShortName(newModule.getName()))) {
                if (newModule.compareTo(get(getShortName(newModule.getName()))) != 0) {
                    return false;
                }
                if (!unregisterModule(newModule.getName())) {
                    return false;
                }
            }

            try {
                // регистрируем модуль в других модулях
                if (ModuleSubscriber.class.isInstance(newModule)) {
                    final List<String> types = ((ModuleSubscriber) newModule).getModuleSubscription();
                    if (types != null) {
                        for (int i = 0; i < types.size(); i++) {
                            types.set(i, getShortName(types.get(i)));
                        }

                        for (String type : types) {
                            if (mModules.containsKey(type)) {
                                ((SmallUnion) mModules.get(type)).register(newModule);
                            }
                        }
                    }
                }

                // регистрируем другие модули в модуле
                if (SmallUnion.class.isInstance(newModule)) {
                    final String type = getShortName(newModule.getName());
                    for (Module module : mModules.values()) {
                        if (ModuleSubscriber.class.isInstance(module)) {
                            final List<String> types = ((ModuleSubscriber) module).getModuleSubscription();
                            if (types != null) {
                                for (int i = 0; i < types.size(); i++) {
                                    types.set(i, getShortName(types.get(i)));
                                }

                                if (types.contains(type)) {
                                    if (!getShortName(module.getName()).equalsIgnoreCase(getShortName(newModule.getName()))) {
                                        ((SmallUnion) newModule).register(module);
                                    }
                                }
                            }
                        }
                    }
                }
                mModules.put(getShortName(newModule.getName()), newModule);
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean registerModule(String name) {
        final Module module = getModuleFactory().create(name);
        if (module != null) {
            return registerModule(module);
        }
        return false;
    }

    @Override
    public boolean unregisterModule(final String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            try {
                final String moduleName = getShortName(name);
                if (mModules.containsKey(moduleName)) {
                    final Module module = mModules.get(moduleName);
                    if (module != null) {
                        if (!module.isPersistent()) {
                            // нельзя отменить подписку у объединения с подписчиками
                            if (SmallUnion.class.isInstance(module)) {
                                if (((SmallUnion) module).hasSubscribers()) {
                                    return false;
                                }
                            }

                            module.onUnRegisterModule();

                            // отменяем регистрацию в других модулях
                            if (ModuleSubscriber.class.isInstance(module)) {
                                final List<String> subscribers = ((ModuleSubscriber) module).getModuleSubscription();
                                for (String subscriber : subscribers) {
                                    final Module moduleSubscriber = mModules.get(getShortName(subscriber));
                                    if (moduleSubscriber != null && SmallUnion.class.isInstance(moduleSubscriber)) {
                                        ((SmallUnion) moduleSubscriber).unregister(module);
                                    }
                                }
                            }
                            mModules.remove(moduleName);
                        }
                    } else {
                        mModules.remove(moduleName);
                    }
                }
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean register(final ModuleSubscriber subscriber) {
        if (subscriber != null && !StringUtils.isNullOrEmpty(subscriber.getName())) {
            try {
                final List<String> types = subscriber.getModuleSubscription();
                if (types != null) {
                    // регистрируемся subscriber в модулях
                    for (String subscriberType : types) {
                        final String moduleName = getShortName(subscriberType);
                        if (mModules.containsKey(moduleName)) {
                            ((SmallUnion) mModules.get(moduleName)).register(subscriber);
                        } else {
                            registerModule(subscriberType);
                            if (mModules.containsKey(moduleName)) {
                                ((SmallUnion) mModules.get(moduleName)).register(subscriber);
                            } else {
                                ErrorModule.getInstance().onError(NAME, "Not found subscriber type: " + subscriberType, false);
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean unregister(final ModuleSubscriber subscriber) {
        if (subscriber != null) {
            try {
                final List<String> types = subscriber.getModuleSubscription();
                if (types != null) {
                    for (int i = 0; i < types.size(); i++) {
                        types.set(i, getShortName(types.get(i)));
                    }

                    for (Module module : mModules.values()) {
                        if (SmallUnion.class.isInstance(module)) {
                            final String subscriberType = getShortName(module.getName());
                            if (!StringUtils.isNullOrEmpty(subscriberType) && types.contains(subscriberType)) {
                                ((SmallUnion) module).unregister(subscriber);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ErrorModule.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setCurrentSubscriber(final ModuleSubscriber subscriber) {
        try {
            if (subscriber != null) {
                final List<String> types = subscriber.getModuleSubscription();
                if (types != null) {
                    for (int i = 0; i < types.size(); i++) {
                        types.set(i, getShortName(types.get(i)));
                    }

                    for (Module module : mModules.values()) {
                        if (Union.class.isInstance(module)) {
                            final String moduleSubscriberType = getShortName(module.getName());
                            if (!StringUtils.isNullOrEmpty(moduleSubscriberType)) {
                                if (types.contains(moduleSubscriberType)) {
                                    ((Union) module).setCurrentSubscriber(subscriber);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ErrorModule.getInstance().onError(NAME, e);
            return false;
        }
        return true;
    }
}
