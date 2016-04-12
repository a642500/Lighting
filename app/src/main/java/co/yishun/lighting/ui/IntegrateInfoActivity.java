package co.yishun.lighting.ui;

import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import co.yishun.lighting.R;
import co.yishun.lighting.api.APIFactory;
import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.api.model.Question;
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
    private QuestionFragment mFragment = QuestionFragment_.builder().build();

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Background(id = CANCEL_WHEN_DESTROY)
    void loadQuestions() {
        safelyDoWithToken((token) -> {
            Response<List<Question>> response = APIFactory.getProcedureAPI().
                    getQuestions(token.userId, token.accessToken, type, 3).execute();

            if (response.isSuccessful()) {
                showQuestions(response.body());
            } else {
                showSnackMsg(R.string.activity_integrate_info_msg_get_question_fail);
                //TODO remove test code
                exit();
            }
        });

        List<Question> fakeQuestions = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            Question question = new Question();
            question.content = "This is my question-" + i;
            question.id = "" + i;
            fakeQuestions.add(question);
        }
        showQuestions(fakeQuestions);

    }

    @UiThread
    void showQuestions(List<Question> questions) {
        mFragment.setQuestions(questions);
    }

    @AfterViews
    void setViews() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commitAllowingStateLoss();
        loadQuestions();
    }

}
