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
    public boolean filterExceptionWithActivity(Exceptionable1<Activity> exceptionable) throws Exception {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.filterExceptionWithActivity(exceptionable);
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
    public <K> K doIfContextNotNull(Callable<K, Context> callable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            return interactiveImpl.doIfContextNotNull(callable);
        else
            return null;
    }

    @Override
    public <K> K doIfActivityNotNull(Callable<K, Activity> callable) {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        if (interactiveImpl != null)
            return interactiveImpl.doIfActivityNotNull(callable);
        else
            return null;
    }

    @Override
    public boolean filterException(Exceptionable exceptionable) throws Exception {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.filterException(exceptionable);
    }

    @Override
    public boolean filterExceptionWithContext(Exceptionable1<Context> exceptionable) throws Exception {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.filterExceptionWithContext(exceptionable);
    }

    @Override
    public boolean filterExceptionWithActivityToken(Exceptionable2<Activity, Token> exceptionable) throws Exception {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.filterExceptionWithActivityToken(exceptionable);
    }

    @Override
    public boolean filterExceptionWithContextToken(Exceptionable2<Context, Token> exceptionable) throws Exception {
        if (interactiveImpl == null) {
            interactiveImpl = (Interactive) getActivity();
        }
        return interactiveImpl == null || interactiveImpl.filterExceptionWithContextToken(exceptionable);
    }
}
