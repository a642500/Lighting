package co.yishun.lighting.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

import co.yishun.lighting.R;
import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.api.model.Question;
import co.yishun.lighting.bean.AudioQuestion;
import co.yishun.lighting.bean.VideoQuestion;
import co.yishun.lighting.ui.common.BaseFragment;
import co.yishun.lighting.ui.view.QuestionView;

/**
 * Created by carlos on 3/28/16.
 */
@EFragment(R.layout.fragment_basic_info)
public class QuestionFragment extends BaseFragment {
    public static final String TAG = "QuestionFragment";

    @ViewById
    QuestionView question0;
    @ViewById
    QuestionView question1;
    @ViewById
    QuestionView question2;
    @ViewById
    View progressBar;
    @FragmentArg
    @Procedure.QuestionType
    String type = Procedure.QUESTION_TYPE_INFO;

    @ViewById
    Toolbar toolbar;
    QuestionView.IQuestion[] mQuestions = new QuestionView.IQuestion[3];
    QuestionView[] questionViews = new QuestionView[3];

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void setToolbar() {
        toolbar.setNavigationOnClickListener(v -> doIfActivityNotNull(NavUtils::navigateUpFromSameTask));
        toolbar.setTitle(R.string.fragment_basic_info_title);
    }

    @AfterViews
    void setQuestionView() {
        questionViews[0] = question0;
        questionViews[1] = question1;
        questionViews[2] = question2;

    }

    @Click
    void finishBtnClicked(View view) {
        MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_SINGLE_TOP).start();
    }

    public void setQuestions(List<Question> questions) {
        for (int i = 0; i < questionViews.length && i < questions.size(); i++) {
            Question question = questions.get(i);
            mQuestions[i] = buildIQuestion(question, i);
            questionViews[i].setQuestion(mQuestions[i]);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode < 3) {
            Uri mediaUri = data.getData();
            File file = new File(mediaUri.getPath());

            final int index = requestCode;
            if (mQuestions[index].buildAnswer(getContext(), file)) {
                questionViews[index].notifyQuestionChanged();
            } else {
                onResultError();
            }
        } else if (resultCode != Activity.RESULT_CANCELED) {
            onResultError();
        }
    }

    private void onResultError() {
        //TODO msg error
    }

    private QuestionView.IQuestion buildIQuestion(final Question question, final int index) {
        switch (type) {
            case Procedure.QUESTION_TYPE_INFO:
                return new AudioQuestion(index, question.content, null);
            case Procedure.QUESTION_TYPE_EXPERIENCE:
            case Procedure.QUESTION_TYPE_VALUES:
            default:
                return new VideoQuestion(index, type, question.content, null);
        }
    }
}
