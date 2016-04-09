package co.yishun.lighting.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import co.yishun.lighting.R;
import co.yishun.lighting.api.model.Question;
import co.yishun.lighting.bean.AudioQuestion;
import co.yishun.lighting.ui.common.BaseFragment;
import co.yishun.lighting.ui.view.QuestionView;

/**
 * Created by carlos on 3/28/16.
 */
@EFragment(R.layout.fragment_basic_info)
public class BasicInfoFragment extends BaseFragment {
    public static final String TAG = "BasicInfoFragment";

    @ViewById
    QuestionView question0;
    @ViewById
    QuestionView question1;
    @ViewById
    QuestionView question2;
    @ViewById
    View progressBar;

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
            mQuestions[i] = buildIQuestion(question, i + 1);
            questionViews[i].setQuestion(mQuestions[i]);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private QuestionView.IQuestion buildIQuestion(final Question question, final int order) {
        return new AudioQuestion(order, question.content, null);
    }
}
