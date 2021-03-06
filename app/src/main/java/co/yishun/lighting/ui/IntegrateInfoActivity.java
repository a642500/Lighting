package co.yishun.lighting.ui;

import android.content.Intent;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import co.yishun.lighting.R;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.api.model.Question;
import co.yishun.lighting.api.model.QuestionList;
import co.yishun.lighting.ui.common.BaseActivity;
import retrofit2.Response;

/**
 * Created by carlos on 3/29/16.
 */
@EActivity(R.layout.activity_info)
public class IntegrateInfoActivity extends BaseActivity {
    public static final String TAG = "IntegrateInfoActivity";
    @ViewById
    FrameLayout container;

    @Extra
    @Procedure.QuestionType
    String type = Procedure.QUESTION_TYPE_INFO;
    private QuestionFragment mFragment;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Background(id = CANCEL_WHEN_DESTROY)
    void loadQuestions() {
        safelyDoWithToken((token) -> {
            Response<QuestionList> response = APIFactory.getProcedureAPI().
                    getQuestions(token.userId, token.accessToken, type, 3).execute();

            if (response.isSuccessful()) {
                showQuestions(response.body().questions);
            } else {
                showSnackMsg(R.string.activity_integrate_info_msg_get_question_fail);
                //TODO remove test code
                exit();
            }
        });
//
//        List<Question> fakeQuestions = new ArrayList<>(3);
//        for (int i = 0; i < 3; i++) {
//            Question question = new Question();
//            question.content = "This is my question-" + i;
//            question.id = "" + i;
//            fakeQuestions.add(question);
//        }
//        showQuestions(fakeQuestions);

    }

    @UiThread
    void showQuestions(List<Question> questions) {
        mFragment.setQuestions(questions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode, resultCode, data);
        // disable origin forwarding implement, because we didn't call startForResult from
        // Fragment, so activity's implementation does not work
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @AfterViews
    void setViews() {
        mFragment = (QuestionFragment) getSupportFragmentManager().findFragmentByTag(type);
        if (mFragment == null) {
            mFragment = QuestionFragment_.builder().type(type).build();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commitAllowingStateLoss();
        loadQuestions();
    }

}
