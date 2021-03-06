package co.yishun.lighting.ui.common;

import android.content.Intent;
import android.net.Uri;

import com.soundcloud.android.crop.Crop;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import java.io.File;

import co.yishun.lighting.util.LogUtil;


/**
 * Copy from OneMoment.
 * Created by Carlos on 2015/8/12.
 */
@EActivity
public abstract class PickCropActivity extends BaseActivity {
    private static final String TAG = "PickCropActivity";
    private Uri mCroppedPictureUri;

    public abstract void onPictureSelectedFailed(Exception e);

    @OnActivityResult(Crop.REQUEST_PICK)
    public void onPictureSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri selectedImage = data.getData();
                mCroppedPictureUri = Uri.fromFile(new File(getCacheDir(), "croppedProfile"));
                Crop.of(selectedImage, mCroppedPictureUri).asSquare().start(this);
            } catch (Exception e) {
                onPictureSelectedFailed(e);
            }
        } else {
            LogUtil.i(TAG, "RESULT_CANCELED");
        }
    }

    @OnActivityResult(Crop.REQUEST_CROP)
    public void onPictureCropped(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            onPictureCropped(mCroppedPictureUri);
        } else {
            mCroppedPictureUri = null;
            LogUtil.i(TAG, "RESULT_CANCELED");
        }
    }

    public abstract void onPictureCropped(Uri uri);

}
