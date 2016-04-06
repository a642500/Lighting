package co.yishun.lighting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import co.yishun.lighting.R;

/**
 * Created by carlos on 4/6/16.
 */
@EActivity
public class AccountActivity extends BaseActivity {
    public static final String TAG = "AccountActivity";
    @Extra
    String phone;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        findViewById(R.id.container);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, SignUpFragment_
                .builder().phone(phone).build()).commitAllowingStateLoss();
    }
}
