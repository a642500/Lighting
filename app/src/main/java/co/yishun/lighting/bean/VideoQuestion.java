package co.yishun.lighting.bean;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import co.yishun.lighting.api.Procedure;
import co.yishun.lighting.ui.PlayActivity_;
import co.yishun.lighting.ui.ShootActivity_;
import co.yishun.lighting.ui.view.QuestionView;
import co.yishun.lighting.util.FileUtil;

/**
 * Created by carlos on 3/29/16.
 */
public class VideoQuestion implements QuestionView.IQuestion {
    public final int mIndex;
    public final String mQuestion;
    @Procedure.QuestionType
    public final String mType;
    private Answer mAnswer;

    public VideoQuestion(int index, @Procedure.QuestionType
    String type, String mQuestion, Answer mAnswer) {
        this.mIndex = index;
        mType = type;
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
    }

    @Override
    public void onRecordAnswer(Context context) {
        ShootActivity_.intent(context).startForResult(
                mIndex);
    }

    @Override
    public int getQuestionIndex() {
        return mIndex;
    }

    @Override
    public boolean isAnswered() {
        return mAnswer != null;
    }

    @Override
    public String getQuestionName() {
        return mQuestion;
    }

    private File getAnswerFile(Context context) {
        return FileUtil.getVideoStoreFile(context, mType, mIndex);
    }

    private Uri getAnswerUri(Context context) {
        return Uri.fromFile(getAnswerFile(context));
    }

    @Override
    public void onPlayAnswer(Context context) {
        PlayActivity_.intent(context).uri(getAnswerUri(context)).start();
    }

    @Override
    public void onDeleteAnswer(Context context) {
        getAnswerFile(context).delete();
        mAnswer = null;
    }

    @Override
    public boolean buildAnswer(Context context, File file) {
        if (file.renameTo(FileUtil.getVideoStoreFile(context, mType, mIndex))) {
            mAnswer = new VideoAnswer();
            return true;
        } else
            return false;
    }

    public class VideoAnswer implements Answer {
        public File getAnswerFile(Context context) {
            return FileUtil.getVideoStoreFile(context, mType, mIndex);
        }

    }

}
