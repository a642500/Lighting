package co.yishun.lighting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import co.yishun.lighting.R;
import co.yishun.lighting.function.Callback;
import co.yishun.lighting.function.Consumer;
import co.yishun.lighting.ui.view.PageIndicatorDot;
import co.yishun.lighting.ui.view.shoot.CameraGLSurfaceView;
import co.yishun.lighting.ui.view.shoot.IShootView;
import co.yishun.lighting.ui.view.shoot.filter.FilterManager;

/**
 * Created by Carlos on 2016/3/22.
 */
@EActivity
public class ShootActivity extends BaseActivity implements Callback, Consumer<File>, IShootView.SecurityExceptionHandler {
    @ViewById
    Toolbar toolbar;
    @ViewById
    IShootView shootView;
    @ViewById(R.id.pageIndicator)
    PageIndicatorDot pageIndicatorDot;
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


        toolbar.setNavigationOnClickListener(v -> exitClicked());

        shootView.setSecurityExceptionHandler(this);
        if (shootView instanceof CameraGLSurfaceView) {
            mCameraGLSurfaceView = ((CameraGLSurfaceView) shootView);
            pageIndicatorDot = ((PageIndicatorDot) findViewById(R.id.pageIndicator));
            pageIndicatorDot.setNum(FilterManager.FilterType.values().length);

            mCameraGLSurfaceView.setFilterListener(index -> pageIndicatorDot.setCurrent(index));
        }

        View shootBtn = findViewById(R.id.shootBtn);
        shootBtn.setOnClickListener(v -> {
            //                    shootBtn.setDuration(1100);
            shootView.record(this, this);
        });

        setControllerBtn();
    }


    @Override
    protected void onResume() {
        if (mCameraGLSurfaceView != null) {
            mCameraGLSurfaceView.onResume();
        }
        super.onResume();
    }


    void exitClicked() {
        this.finish();
    }

    @Click
    void flashlightSwitchClicked() {

    }

    @Click
    void cameraSwitchClicked() {

    }

    @Click
    void shootBtnClicked() {

    }

    @Override
    public void call() {

    }

    @Override
    public void accept(File file) {

    }

    @Override
    public void onHandler(SecurityException e) {

    }
}
