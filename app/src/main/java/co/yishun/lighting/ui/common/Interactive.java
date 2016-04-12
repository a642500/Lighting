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
    String CANCEL_WHEN_PAUSE = "cancel_when_pause";
    String CANCEL_WHEN_DESTROY = "cancel_when_destroy";

    void showSnackMsg(String msg);

    void showSnackMsg(@StringRes int msgRes);

    void showProgress();

    void showProgress(String msg);

    void showProgress(@StringRes int msgRes);


    void hideProgress();

    void exit();

    void doIfContextNotNull(Callable<Context> callable);

    void doIfActivityNotNull(Callable<Activity> callable);


    /**
     * filter {@link IOException} and {@link AccountManager.UnauthorizedException} to handle it.
     *
     * @param exceptionable the runnable which may throw {@link IOException}
     *                      and {@link AccountManager.UnauthorizedException}
     * @return whether exception occurs.
     */
    boolean safelyDo(Exceptionable exceptionable);

    boolean safelyDoWithContext(Exceptionable1<Context> exceptionable);

    boolean safelyDoWithActivity(Exceptionable1<Activity> exceptionable);

    boolean safelyDoWithActivityToken(Exceptionable2<Activity, Token> exceptionable);

    boolean safelyDoWithContextToken(Exceptionable2<Context, Token> exceptionable);

    boolean safelyDoWithToken(Exceptionable1<Token> exceptionable);

    interface Callable<T> {
        void call(T t);
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
