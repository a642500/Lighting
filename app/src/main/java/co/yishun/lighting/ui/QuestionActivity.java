package co.yishun.lighting.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import co.yishun.lighting.R;
import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.ui.common.BaseActivity;

/**
 * Created by carlos on 5/1/16.
 */
@EActivity(R.layout.activity_question)
public class QuestionActivity extends BaseActivity {
    private static final String TAG = "QuestionActivity";
    @Extra
    @Procedure.QuestionType
    String type = Procedure.QUESTION_TYPE_INFO;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void setUp() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                QuestionFragment_.builder().type(type).build())
                .addToBackStack("home").commitAllowingStateLoss();
    }
}
