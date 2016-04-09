package co.yishun.lighting.ui.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import java.io.IOException;

import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.api.model.Token;

/**
 * Created by carlos on 4/9/16.
 */
@SuppressWarnings("unused")
public interface Interactive {
    void showSnackMsg(String msg);

    void showSnackMsg(@StringRes int msgRes);

    void showProgress();

    void showProgress(String msg);

    void showProgress(@StringRes int msgRes);


    void hideProgress();

    void exit();

    <K> K doIfContextNotNull(Callable<K, Context> callable);

    <K> K doIfActivityNotNull(Callable<K, Activity> callable);

    /**
     * filter {@link IOException} and {@link AccountManager.UnauthorizedException} to handle it.
     *
     * @param exceptionable the runnable which may throw {@link IOException}
     *                      and {@link AccountManager.UnauthorizedException}
     * @return whether exception occurs.
     * @throws Exception except for {@link IOException} and {@link AccountManager.UnauthorizedException}
     */
    boolean filterException(Exceptionable exceptionable) throws Exception;

    boolean filterExceptionWithContext(Exceptionable1<Context> exceptionable) throws Exception;

    boolean filterExceptionWithActivity(Exceptionable1<Activity> exceptionable) throws Exception;

    boolean filterExceptionWithActivityToken(Exceptionable2<Activity, Token> exceptionable) throws Exception;

    boolean filterExceptionWithContextToken(Exceptionable2<Context, Token> exceptionable) throws Exception;

    interface Callable<K, T> {
        K call(T t);
    }

    interface Exceptionable {
        void call() throws Exception;
    }

    interface Exceptionable1<T> {
        void call(T t) throws Exception;
    }

    interface Exceptionable2<T, K> {
        void call(T t, K k) throws Exception;
    }

}
