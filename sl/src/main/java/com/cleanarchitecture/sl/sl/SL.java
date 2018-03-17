package com.cleanarchitecture.sl.sl;

import com.cleanarchitecture.sl.repository.DbProvider;

@SuppressWarnings("unused")
public class SL extends AbsServiceLocator {

    public static final String NAME = SL.class.getName();

    private static volatile SL sInstance;
    private ModuleFactory mModuleFactory = new ServiceLocatorModuleFactory();

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (SL.class) {
                if (sInstance == null) {
                    sInstance = new SL();
                }
            }
        }
    }

    public static SL getInstance() {
        if (sInstance == null) {
            instantiate();
        }
        return sInstance;
    }

    private SL() {
        onStart();
    }

    @Override
    public void onStart() {
        // Модуль приложения
        registerModule(ApplicationModule.getInstance());

        // Модуль регистрации ошибок в приложении
        registerModule(ErrorModule.getInstance());

        // Объединение подписчиков почтовых сообщений
        registerModule(MailUnion.getInstance());

        // Модуль регистрации падения приложения
        registerModule(CrashModule.NAME);

        // Объединение Activity
        registerModule(ActivityUnion.NAME);

        // Объединение презенторов
        registerModule(PresenterUnion.NAME);

        // Объединение Observables
        registerModule(ObservableUnion.NAME);

        // UseCase модуль
        registerModule(UseCasesModule.NAME);

        // Модуль работы с БД
        registerModule(DbProvider.NAME);

        // Модуль выполнения запросов
        registerModule(RequestModule.NAME);
    }

    @Override
    public ModuleFactory getModuleFactory() {
        return mModuleFactory;
    }

    @Override
    public void onFinish() {
        SLUtil.getActivityUnion().hideKeyboard();
        SLUtil.getMailUnion().onFinishApplication();
        SLUtil.getPresenterUnion().onFinishApplication();
        SLUtil.getActivityUnion().onFinishApplication();
    }

    @Override
    public String getName() {
        return NAME;
    }

}
