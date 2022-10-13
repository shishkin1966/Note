package com.cleanarchitecture.sl.sl;

import androidx.room.RoomDatabase;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import com.cleanarchitecture.common.utils.SafeUtils;
import com.cleanarchitecture.sl.mail.Mail;
import com.cleanarchitecture.sl.repository.DbProvider;
import com.cleanarchitecture.sl.repository.IDbProvider;
import com.cleanarchitecture.sl.repository.INetImageProvider;
import com.cleanarchitecture.sl.repository.NetImageProvider;
import com.cleanarchitecture.sl.state.ViewStateObserver;
import com.cleanarchitecture.sl.ui.activity.AbsActivity;
import com.cleanarchitecture.sl.ui.activity.AbsContentActivity;
import com.cleanarchitecture.sl.ui.fragment.AbsContentFragment;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;


import java.util.List;

/**
 * Инструменты администратора
 */
public class SLUtil {

    public static Context getContext() {
        return ApplicationModule.getInstance();
    }

    public static IActivityUnion getActivityUnion() {
        return SL.getInstance().get(ActivityUnion.NAME);
    }

    public static IUseCasesModule getUseCasesUnion() {
        return SL.getInstance().get(UseCasesModule.NAME);
    }

    public static IPresenterUnion getPresenterUnion() {
        return SL.getInstance().get(PresenterUnion.NAME);
    }

    public static <C> C getActivity() {
        final IRoutingUnion Union = getRoutingUnion();
        if (Union != null) {
            return Union.getActivity();
        }
        return null;
    }

    public static <C> C getActivity(final String name) {
        final IRoutingUnion Union = getRoutingUnion();
        if (Union != null) {
            return Union.getActivity(name);
        }
        return null;
    }

    public static <C> C getActivity(final String name, final boolean validate) {
        final IRoutingUnion Union = getRoutingUnion();
        if (Union != null) {
            return Union.getActivity(name, validate);
        }
        return null;
    }

    public static IMailUnion getMailUnion() {
        return SL.getInstance().get(MailUnion.NAME);
    }

    public static IRoutingUnion getRoutingUnion() {
        return SL.getInstance().get(RoutingUnion.NAME);
    }

    public static IDbProvider getDbProvider() {
        return SL.getInstance().get(DbProvider.NAME);
    }

    public static INetImageProvider getNetImageProvider() {
        return SL.getInstance().get(NetImageProvider.NAME);
    }

    public static IRequestModule getRequestModule() {
        return SL.getInstance().get(RequestModule.NAME);
    }

    public static IObservableUnion getObservableUnion() {
        return SL.getInstance().get(ObservableUnion.NAME);
    }

    /**
     * Зарегистрировать подписчика модуля
     *
     * @param subscriber подписчик модуля
     */
    public static void register(final ModuleSubscriber subscriber) {
        SL.getInstance().register(subscriber);
    }

    /**
     * Отменить регистрацию подписчика модуля
     *
     * @param subscriber подписчик модуля
     */
    public static void unregister(final ModuleSubscriber subscriber) {
        SL.getInstance().unregister(subscriber);
    }

    /**
     * Сделать подписчика текущим
     *
     * @param subscriber подписчик модуля
     */
    public static void setCurrentSubscriber(final ModuleSubscriber subscriber) {
        SL.getInstance().setCurrentSubscriber(subscriber);
    }

    /**
     * Читать почту
     *
     * @param subscriber почтовый подписчик
     */
    public static void readMail(final MailSubscriber subscriber) {
        final IMailUnion union = SL.getInstance().get(MailUnion.NAME);
        if (union != null) {
            final List<Mail> list = union.getMail(subscriber);
            for (Mail mail : list) {
                if (subscriber.getState() == ViewStateObserver.STATE_RESUME) {
                    mail.read(subscriber);
                    union.removeMail(mail);
                }
            }
        }
    }

    /**
     * Добавить почтовое сообщение
     *
     * @param mail почтовое сообщение
     */
    public static void addMail(final Mail mail) {
        final IMailUnion union = getMailUnion();
        if (union != null) {
            union.addMail(mail);
        }
    }

    /**
     * Получить content fragment.
     *
     * @return the content fragment
     */
    public static AbsContentFragment getContentFragment() {
        final IRoutingUnion union = getRoutingUnion();
        if (union != null) {
            final AbsActivity activity = union.getActivity();
            if (activity != null && AbsContentActivity.class.isInstance(activity)) {
                return SafeUtils.cast(((AbsContentActivity) activity).getContentFragment(AbsContentFragment.class));
            }
        }
        return null;
    }

    public static boolean isGooglePlayServices() {
        return true;
    }

    /**
     * Контролировать наличие и версию Google Play Services
     */
/*
    public static boolean isGooglePlayServices() {
        final Context context = ApplicationModule.getInstance().getApplicationContext();
        if (context != null) {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (ConnectionResult.SUCCESS == resultCode) {
                return true;
            }
        }
        return false;
    }
*/

    /**
     * Проверить разрешение права приложения
     *
     * @param permission наименование права
     * @return true - право приложению разрешено
     */
    public static boolean checkPermission(final String permission) {
        return getStatusPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Получить статус права приложения
     *
     * @param permission наименование права
     * @return статус права
     */
    public static int getStatusPermission(final String permission) {
        final Context context = ApplicationModule.getInstance().getApplicationContext();
        if (context != null) {
            return ActivityCompat.checkSelfPermission(context, permission);
        } else {
            return PackageManager.PERMISSION_DENIED;
        }
    }

    /**
     * Проверить существование валидной activity.
     *
     * @return true - существует
     */
    public static boolean isValidActivity() {
        final IRoutingUnion Union = getRoutingUnion();
        if (Union != null) {
            final AbsActivity activity = Union.getActivity();
            if (activity != null && activity.validate() && (activity.getState() == ViewStateObserver.STATE_RESUME || activity.getState() == ViewStateObserver.STATE_PAUSE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получить БД
     *
     * @param <T>          тип БД
     * @param klass        class БД
     * @param databaseName имя БД
     * @return the db
     */
    public static <T extends RoomDatabase> T getDb(final Class<T> klass, final String databaseName) {
        final IDbProvider provider = getDbProvider();
        if (provider != null) {
            return (T) provider.getDb(klass, databaseName);
        }
        return null;
    }

    /**
     * Получить БД
     *
     * @return the db
     */
    public static <T extends RoomDatabase> T getDb() {
        final IDbProvider provider = getDbProvider();
        if (provider != null) {
            return (T) provider.getDb();
        }
        return null;
    }
}
