package co.yishun.lighting.ui;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import co.yishun.lighting.R;
import co.yishun.lighting.ui.common.BaseActivity;

/**
 * Created by Carlos on 2016/4/10.
 */
@EActivity(R.layout.activity_record)
public class RecordActivity extends BaseActivity {
    private static final String TAG = "RecordActivity";

    @Extra
    String questionName;


    @Override
    public String getPageInfo() {
        return TAG;
    }


}
