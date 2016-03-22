package co.yishun.lighting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import co.yishun.lighting.R;

/**
 * Created by Carlos on 2016/3/22.
 */
public class ShootActivity extends BaseActivity {
    @Override
    public String getPageInfo() {
        return "ShootActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shoot);
    }
}
