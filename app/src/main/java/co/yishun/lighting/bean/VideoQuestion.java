package co.yishun.lighting.bean;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

import co.yishun.lighting.ui.ShootActivity_;
import co.yishun.lighting.ui.view.QuestionView;
import co.yishun.lighting.util.FileUtil;

/**
 * Created by carlos on 3/29/16.
 */
public class VideoQuestion implements QuestionView.IQuestion {
    public final int mOrder;
    public final String mQuestion;
    public final Answer mAnswer;
    private MediaPlayer mMediaPlayer;

    public VideoQuestion(int mOrder, String mQuestion, Answer mAnswer) {
        this.mOrder = mOrder;
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
    }

    @Override
    public void onRecordAnswer(Context context) {
        ShootActivity_.intent(context).start();
    }

    @Override
    public int getQuestionOrder() {
        return mOrder;
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
        return FileUtil.getAudioStoreFile(context, mOrder);
    }

    @Override
    public void onPlayAnswer(Context context) {
        if (mMediaPlayer == null) {
            File audio = getAnswerFile(context);
            if (audio.length() > 0) {
                mMediaPlayer = MediaPlayer.create(context, Uri.fromFile(audio));
            }
        }
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.pause();
            else
                mMediaPlayer.start();
        }
    }

    @Override
    public void onDeleteAnswer(Context context) {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        getAnswerFile(context).delete();
    }

    public class Answer {
    }

}
