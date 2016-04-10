package co.yishun.lighting.ui;

import org.androidannotations.annotations.Extra;

import co.yishun.lighting.api.model.Question;
import co.yishun.lighting.ui.common.BaseActivity;

/**
 * Created by Carlos on 2016/4/10.
 */
public class RecordActivity extends BaseActivity {
    private static final String TAG = "RecordActivity";

    @Extra
    Question question;


    @Override
    public String getPageInfo() {
        return TAG;
    }


}
