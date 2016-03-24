package co.yishun.lighting.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageSwitcher;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import co.yishun.lighting.R;
import co.yishun.lighting.function.Callback;
import co.yishun.lighting.function.Consumer;
import co.yishun.lighting.ui.view.PageIndicatorDot;
import co.yishun.lighting.ui.view.shoot.CameraGLSurfaceView;
import co.yishun.lighting.ui.view.shoot.IShootView;
import co.yishun.lighting.ui.view.shoot.filter.FilterManager;
import co.yishun.lighting.util.FileUtil;
import co.yishun.lighting.util.LogUtil;
import co.yishun.lighting.util.video.VideoCommand;
import co.yishun.lighting.util.video.VideoConvert;

/**
 * Created by Carlos on 2016/3/22.
 */
@EActivity
public class ShootActivity extends BaseActivity implements Callback, Consumer<File>, IShootView.SecurityExceptionHandler {
    private static final String TAG = "ShootActivity";
    @ViewById
    Toolbar toolbar;
    IShootView shootView;
    @ViewById(R.id.pageIndicator)
    PageIndicatorDot pageIndicatorDot;
    @ViewById
    View cameraSwitch;
    @ViewById
    ImageSwitcher recordFlashSwitch;
    @Nullable
    private CameraGLSurfaceView mCameraGLSurfaceView;
    private boolean flashOn = false;

    @Override
    public String getPageInfo() {
        return "ShootActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);


        shootView = (IShootView) findViewById(R.id.shootView);
        toolbar.setNavigationOnClickListener(v -> exitClicked());

        shootView.setSecurityExceptionHandler(this);
        if (shootView instanceof CameraGLSurfaceView) {
            mCameraGLSurfaceView = ((CameraGLSurfaceView) shootView);
            pageIndicatorDot = ((PageIndicatorDot) findViewById(R.id.pageIndicator));
            pageIndicatorDot.setNum(FilterManager.FilterType.values().length);

            mCameraGLSurfaceView.setFilterListener(index -> pageIndicatorDot.setCurrent(index));
        }

        setControlBtn();
    }

    @Click
    void shootBtnClicked() {
        shootView.record(this, this);
        //TODO update record btn and other btns
    }

    private void setControlBtn() {
        flashOn = false;
        cameraSwitch.setEnabled(shootView.isFrontCameraAvailable());
        recordFlashSwitch.setEnabled(shootView.isFlashlightAvailable());
        recordFlashSwitch.setDisplayedChild(0);
    }

    @Click
    void cameraSwitchClicked(View view) {
        shootView.switchCamera(!shootView.isBackCamera());
        setControlBtn();
    }

    @Click
    void recordFlashSwitchClicked(View view) {
        LogUtil.i(TAG, "flashlight switch, from " + flashOn + " to " + !flashOn);
        flashOn = !flashOn;
        shootView.setFlashlightOn(flashOn);
        recordFlashSwitch.setDisplayedChild(flashOn ? 1 : 0);
    }

    @Override
    protected void onResume() {
        if (mCameraGLSurfaceView != null) {
            mCameraGLSurfaceView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mCameraGLSurfaceView != null) {
            mCameraGLSurfaceView.onPause();
        }
        super.onPause();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        if (mCameraGLSurfaceView != null) {
            mCameraGLSurfaceView.onDestroy();
        }
        super.onDestroy();
    }


    void exitClicked() {
        this.finish();
    }

    @Override
    public void call() {
        LogUtil.i(TAG, "start record callback");
    }

    @Override
    public void accept(File file) {
        LogUtil.i(TAG, "accept: " + file);
        if (shootView instanceof CameraGLSurfaceView)
            delayStart(file);
        else {
            showProgress();
            delayAccept(file);
        }
    }

    @UiThread(delay = 800)
    void delayAccept(File file) {
        File newFile = FileUtil.getVideoCacheFile(this);
        new VideoConvert(this).setFiles(file, newFile).setListener(new VideoCommand.VideoCommandListener() {
            @Override
            public void onSuccess(VideoCommand.VideoCommandType type) {
                file.delete();
                delayStart(newFile);
                hideProgress();
            }

            @Override
            public void onFail(VideoCommand.VideoCommandType type) {
                hideProgress();
            }
        }).start();
    }

    @UiThread(delay = 200)
    void delayStart(File file) {
//        TagCreateActivity_.intent(this).forDiary(forDiary).forWorld(forWorld).forToday(forToday).world(world).videoPath(file.getPath()).start();
        this.finish();
    }

    @Override
    public void onHandler(SecurityException e) {
        LogUtil.e(TAG, "", e);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).positiveText(R.string.activity_shoot_permission_error_ok).content(R.string.activity_shoot_permission_error_msg).title(R.string.activity_shoot_permission_error_title).cancelable(false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            builder.negativeText(R.string.activity_shoot_permission_error_settings);
            builder.callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    ShootActivity.this.finish();
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    try {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                    }
                }
            });
        } else {
            builder.callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    ShootActivity.this.finish();
                }
            });
        }
        builder.show();
        //TODO add help btn to guide user to how enable permission for three-party rom
    }
}
