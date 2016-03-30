package co.yishun.lighting.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.yishun.lighting.R;

/**
 * Created by carlos on 3/24/16.
 */

@SuppressWarnings("unused")
public class QuestionView extends RelativeLayout {
    TextView questionTextView;
    TextView orderTextView;
    View deleteBtn;
    View arrowIcon;
    View playBtn;
    private IQuestion mQuestion;
    @ColorInt
    private int mAccentColor;
    @ColorInt
    private int mPrimaryColor;

    public QuestionView(Context context) {
        super(context);
        init();
    }

    public QuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QuestionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mAccentColor = getResources().getColor(R.color.colorAccent);
        mPrimaryColor = getResources().getColor(R.color.textColorPrimary);
        this.setBackgroundResource(R.drawable.ic_basic_item);
        setGravity(CENTER_VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.view_question, this, true);
        questionTextView = ((TextView) findViewById(R.id.questionTextView));
        orderTextView = ((TextView) findViewById(R.id.orderTextView));
        deleteBtn = findViewById(R.id.deleteBtn);
        playBtn = findViewById(R.id.playBtn);
        arrowIcon = findViewById(R.id.arrowIcon);


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !(mQuestion != null && mQuestion.isAnswered()) && super.onInterceptTouchEvent(ev);
    }

    public void setQuestion(@NonNull IQuestion question) {
        mQuestion = question;
        questionTextView.setText(question.getQuestionName());
        orderTextView.setText(String.valueOf(question.getQuestionOrder()));
        deleteBtn.setOnClickListener(v -> mQuestion.onDeleteAnswer(getContext()));
        playBtn.setOnClickListener(v -> mQuestion.onPlayAnswer(getContext()));
        this.setOnClickListener(v -> mQuestion.onRecordAnswer(getContext()));
        if (question.isAnswered()) {
            deleteBtn.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.VISIBLE);
            arrowIcon.setVisibility(View.INVISIBLE);
            questionTextView.setTextColor(mAccentColor);
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
            playBtn.setVisibility(View.INVISIBLE);
            arrowIcon.setVisibility(View.VISIBLE);
            questionTextView.setTextColor(mPrimaryColor);
        }
    }

    public void notifyQuestionChanged() {
        setQuestion(mQuestion);
    }

    public interface IQuestion {
        int getQuestionOrder();

        boolean isAnswered();

        String getQuestionName();

        void onPlayAnswer(Context context);

        void onDeleteAnswer(Context context);

        void onRecordAnswer(Context context);
    }

}
