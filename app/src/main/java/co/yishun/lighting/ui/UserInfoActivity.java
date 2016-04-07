package co.yishun.lighting.ui;

import android.net.Uri;

import org.androidannotations.annotations.EActivity;

import co.yishun.lighting.R;
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

    @Override
    public void onPictureCropped(Uri uri) {

    }
}
