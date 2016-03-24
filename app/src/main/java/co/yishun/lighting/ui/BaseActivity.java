package co.yishun.lighting.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import co.yishun.lighting.R;
import co.yishun.lighting.util.LogUtil;

import static java.lang.String.valueOf;

/**
 * Copy from OneMoment.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseActivity";
    //set it false, if we only take this activity's fragments into count. else set it true, and give a page name.
    protected boolean mIsPage = true;
    private MaterialDialog mProgressDialog;
    @SuppressWarnings("FieldCanBeLocal")
    private String statusTag = "status";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        PushAgent.getInstance(this).onAppStart();
    }

    @SuppressWarnings("unused")
    protected void setStatusBarOnKitKat() {
        FrameLayout content = (FrameLayout) findViewById(android.R.id.content);
        //noinspection ConstantConditions
        View status = content.findViewWithTag(statusTag);
        if (status != null) content.removeView(status);

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        status = new View(this);
        status.setBackgroundResource(R.color.colorPrimaryDark);
        status.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, result));
        status.setTag(statusTag);
        content.addView(status, 0);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
//        if (!(this instanceof SplashActivity || this instanceof EntryActivity || this instanceof AccountActivity)) {
//            setStatusBarOnKitKat();
//        }
    }


    @CallSuper
    @NonNull
    public View getSnackbarAnchorWithView(@Nullable View view) {
        //noinspection ConstantConditions
        return view != null ? view : findViewById(android.R.id.content);
    }

    public abstract String getPageInfo();

    @UiThread
    public void showSnackMsg(String msg) {
        Snackbar.make(getSnackbarAnchorWithView(null), msg, Snackbar.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unused")
    public void showSnackMsg(@StringRes int msgRes) {
        showSnackMsg(getString(msgRes));
    }

    @SuppressWarnings("unused")
    public void showProgress() {
        showProgress(R.string.progress_loading_msg);
    }

    @UiThread
    public void showProgress(String msg) {
        if (mProgressDialog == null)
            mProgressDialog = new MaterialDialog.Builder(this).theme(Theme.LIGHT).cancelable(false).content(msg).progress(true, 0).build();
        mProgressDialog.setContent(msg);
        mProgressDialog.show();
    }

    public void showProgress(@StringRes int msgRes) {
        showProgress(getString(msgRes));
    }

    @UiThread
    @SuppressWarnings("unused")
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = getPageInfo();
        LogUtil.d(name, valueOf(mIsPage));
        if (mIsPage) {
            MobclickAgent.onPageStart(name);
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsPage) {
            MobclickAgent.onPageEnd(getPageInfo());
        }
        MobclickAgent.onPause(this);
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
//        BaseWebFragment webFragment = (BaseWebFragment) getSupportFragmentManager()
//                .findFragmentByTag(BaseWebFragment.TAG_WEB);
//        if (webFragment != null && webFragment.canGoBack()) {
//            webFragment.goBack();
//        } else {
//            supportFinishAfterTransition();
//        }
    }

    @UiThread(delay = 300)
    @SuppressWarnings("unused")
    public void exit() {
        finish();
    }

}
