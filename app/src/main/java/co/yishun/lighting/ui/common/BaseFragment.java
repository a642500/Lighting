package co.yishun.lighting.ui.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

import co.yishun.lighting.api.model.Token;

/**
 * Common base
 * <p>
 * Created by yyz on 8/3/15.
 */
public abstract class BaseFragment extends Fragment implements Interactive {
    //set it true and give a page name in setPageInfo(), if we take this fragment into count.
    protected boolean mIsPage = true;
    private Interactive interactiveImpl;

    public abstract String getPageInfo();

    @Override
    public void onResume() {
        super.onResume();
        if (mIsPage) {
            MobclickAgent.onPageStart(getPageInfo());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsPage) {
            MobclickAgent.onPageEnd(getPageInfo());
        }
    }

    @Override
    public boolean safelyDoWithActivity(Exceptionable1<Activity> exceptionable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.safelyDoWithActivity(exceptionable);
    }

    @Override
    public void showSnackMsg(String msg) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.showSnackMsg(msg);
    }

    @Override
    public void showSnackMsg(@StringRes int msgRes) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.showSnackMsg(msgRes);
    }

    @Override
    public void showProgress() {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.showProgress();
    }

    @Override
    public void showProgress(String msg) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.showProgress(msg);
    }

    @Override
    public void showProgress(@StringRes int msgRes) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.showProgress(msgRes);
    }

    @Override
    public void hideProgress() {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.hideProgress();
    }

    @Override
    public void exit() {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.exit();
    }

    @Override
    public void doIfContextNotNull(Callable<Context> callable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.doIfContextNotNull(callable);
    }

    @Override
    public void doIfActivityNotNull(Callable<Activity> callable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            interactiveImpl.doIfActivityNotNull(callable);
    }

    @Override
    public boolean safelyDo(Exceptionable exceptionable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.safelyDo(exceptionable);
    }

    @Override
    public boolean safelyDoWithContext(Exceptionable1<Context> exceptionable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.safelyDoWithContext(exceptionable);
    }

    @Override
    public boolean safelyDoWithToken(Exceptionable1<Token> exceptionable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.safelyDoWithToken(exceptionable);
    }

    @Override
    public boolean safelyDoWithActivityToken(Exceptionable2<Activity, Token> exceptionable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.safelyDoWithActivityToken(exceptionable);
    }

    @Override
    public boolean safelyDoWithContextToken(Exceptionable2<Context, Token> exceptionable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.safelyDoWithContextToken(exceptionable);
    }
}
