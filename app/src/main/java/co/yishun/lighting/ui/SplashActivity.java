package co.yishun.lighting.ui;

import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;

import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.ui.common.BaseActivity;

/**
 * Created by Carlos on 2016/3/23.
 */
@EActivity(R.layout.activity_splash)
@Fullscreen
public class SplashActivity extends BaseActivity {
    @Override
    public String getPageInfo() {
        return "SplashActivity";
    }


    @AfterViews
    @UiThread(delay = 1600)
    void endWithStartMain() {
        if (AccountManager.isLogin(this))
            MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
        else
            LoginActivity_.intent(this).start();
        this.finish();
    }
}
