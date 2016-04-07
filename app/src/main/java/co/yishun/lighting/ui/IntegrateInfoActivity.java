package co.yishun.lighting.ui;

import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.ui.common.BaseActivity;

/**
 * Created by carlos on 3/29/16.
 */
@EActivity(R.layout.activity_info)
public class IntegrateInfoActivity extends BaseActivity {
    public static final String TAG = "IntegrateInfoActivity";
    @ViewById
    FrameLayout container;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void setFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, BasicInfoFragment_.builder().build()).commitAllowingStateLoss();
    }
}
