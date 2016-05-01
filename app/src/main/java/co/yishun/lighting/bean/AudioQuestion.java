package co.yishun.lighting.bean;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

import co.yishun.lighting.ui.RecordActivity_;
import co.yishun.lighting.ui.view.QuestionView;
import co.yishun.lighting.util.FileUtil;

/**
 * Created by carlos on 3/29/16.
 */
public class AudioQuestion implements QuestionView.IQuestion {
    public final int mIndex;
    public final String mQuestion;
    private Answer mAnswer;
    private MediaPlayer mMediaPlayer;

    public AudioQuestion(int index, String mQuestion, Answer mAnswer) {
        this.mIndex = index;
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
    }


    @Override
    public void onRecordAnswer(Context context) {
        RecordActivity_.intent(context).questionName(this.getQuestionName()).startForResult(mIndex);
    }

    @Override
    public boolean buildAnswer(Context context, File file) {
        if (file.renameTo(FileUtil.getAudioStoreFile(context, mIndex))) {
            mAnswer = new AudioAnswer();
            return true;
        } else
            return false;
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

    @Override
    public void onPlayAnswer(Context context) {
        if (mMediaPlayer == null) {
            File audio = mAnswer.getAnswerFile(context);
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
        mAnswer.getAnswerFile(context).delete();
        mAnswer = null;
    }

    public class AudioAnswer implements Answer {
        public File getAnswerFile(Context context) {
            return FileUtil.getAudioStoreFile(context, mIndex);
        }
    }

}
