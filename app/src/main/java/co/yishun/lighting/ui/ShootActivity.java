package co.yishun.lighting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;

/**
 * Created by Carlos on 2016/3/22.
 */
@EActivity
public class ShootActivity extends BaseActivity {
    @ViewById
    Toolbar toolbar;

    @Override
    public String getPageInfo() {
        return "ShootActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitClicked();
            }
        });
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
}
