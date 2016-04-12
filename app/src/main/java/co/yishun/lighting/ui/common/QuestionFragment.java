package co.yishun.lighting.ui.common;

import java.util.List;

import co.yishun.lighting.api.model.Question;

/**
 * Created by carlos on 4/12/16.
 */
public abstract class QuestionFragment extends BaseFragment {
    public abstract void setQuestions(List<Question> questions);
}
