package co.yishun.lighting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.ui.common.BaseActivity;
import co.yishun.lighting.ui.common.BaseWebFragment_;
import co.yishun.lighting.ui.view.AnimUtil;
import co.yishun.lighting.ui.view.ResideLayout;


@EActivity
public class MainActivity extends BaseActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    ResideLayout resideLayout;
    @ViewById
    ImageView profileImageView;
    @ViewById
    TextView nicknameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = BaseWebFragment_.builder().
                mUrl("http://devlightup.yishun.co:61336/static/video_index.html").build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, "others")
                .commitAllowingStateLoss();

        toolbar.setNavigationOnClickListener(v -> resideLayout.openPane());
    }

    @AfterViews
    void setViews() {
        Picasso.with(this).load(AccountManager.getUserInfo(this).portrait)
                .error(R.drawable.pic_profile_default).into(profileImageView);
        nicknameTextView.setText(AccountManager.getUserInfo(this).nickname);
    }

    @Override
    public String getPageInfo() {
        return "MainActivity";
    }

    public void onNavigationItemClicked(View view) {
        resideLayout.closePane();
        delayNavigation(view);
    }

    @UiThread(delay = 300)
    void delayNavigation(View view) {
        Fragment fragment;

        switch (view.getId()) {
            case R.id.navigation_item_lightup:
                fragment = getSupportFragmentManager().findFragmentByTag("others");
                if (fragment == null) {
                    fragment = BaseWebFragment_.builder().
                            mUrl("http://devlightup.yishun.co:61336/static/video_index.html").build();
                }
                AnimUtil.alpha(getSupportFragmentManager())
                        .replace(R.id.container, fragment, "others")
                        .commitAllowingStateLoss();
                break;
            case R.id.navigation_item_mssage:
                fragment = getSupportFragmentManager().findFragmentByTag("message");
                if (fragment == null) {
                    fragment = BaseWebFragment_.builder().
                            mUrl("http://devlightup.yishun.co:61336/static/message.html").build();
                }
                AnimUtil.alpha(getSupportFragmentManager())
                        .replace(R.id.container, fragment, "message")
                        .commitAllowingStateLoss();
                break;
            case R.id.navigation_item_profile:
                fragment = getSupportFragmentManager().findFragmentByTag("personal");
                if (fragment == null) {
                    fragment = BaseWebFragment_.builder().
                            mUrl("http://devlightup.yishun.co:61336/static/personal.html").build();
                }
                AnimUtil.alpha(getSupportFragmentManager())
                        .replace(R.id.container, fragment, "personal")
                        .commitAllowingStateLoss();

                break;
            case R.id.navigation_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    @Click
    void profileImageViewClicked(View view) {
        UserInfoActivity_.intent(this).start();
    }
}
