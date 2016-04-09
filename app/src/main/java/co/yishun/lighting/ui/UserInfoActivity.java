package co.yishun.lighting.ui;

import android.net.Uri;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import co.yishun.lighting.R;
import co.yishun.lighting.account.AccountManager;
import co.yishun.lighting.api.model.User;
import co.yishun.lighting.ui.account.UserInfoFragment;
import co.yishun.lighting.ui.account.UserInfoFragment_;
import co.yishun.lighting.ui.common.PickCropActivity;


/**
 * Created by Jinge on 2015/11/12.
 */
@EActivity(R.layout.activity_user_info)
public class UserInfoActivity extends PickCropActivity {
    private static final String TAG = "UserInfoActivity";

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    public void onPictureSelectedFailed(Exception e) {

    }


    @AfterViews
    void setViews() {
        User user = AccountManager.getUserInfo(this);

        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .replace(R.id.container, UserInfoFragment_
                        .builder().phone(user.phoneNumber).token(user.getToken())
                        .editMode(UserInfoFragment.EDIT_MODE_COMMIT_EVERY_TIME).build())
                .commitAllowingStateLoss();
    }


    @Override
    public void onPictureCropped(Uri uri) {

    }
}
