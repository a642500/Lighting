package co.yishun.lighting.ui;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import co.yishun.lighting.Constants;
import co.yishun.lighting.R;
import co.yishun.lighting.ui.common.BaseActivity;
import co.yishun.lighting.ui.view.AnimationUtil;
import co.yishun.lighting.util.FileUtil;

/**
 * Created by Carlos on 2016/4/10.
 */
@EActivity(R.layout.activity_record)
public class RecordActivity extends BaseActivity implements MediaRecorder.OnInfoListener {
    public static final int STATUS_PREPARED = UIStatus.STATUS_SELF_START;
    public static final int STATUS_RECORDING = STATUS_PREPARED + 1;
    public static final int STATUS_RECORDED = STATUS_RECORDING + 1;
    public static final int STATUS_PLAYING = STATUS_RECORDED + 1;
    public static final int STATUS_PAUSE = STATUS_PLAYING + 1;
    public static final int STATUS_PLAYED = STATUS_PAUSE + 1;
    public static final int STATUS_ERROR = STATUS_PLAYED + 1;
    private static final String TAG = "RecordActivity";
    @Extra
    String questionName;
    @ViewById
    ImageButton recordBtn;
    @ViewById
    View redoBtn;
    @ViewById
    View finishBtn;
    @ViewById
    ProgressBar progressBar;
    MediaRecorder mRecorder;
    MediaPlayer mMediaPlayer;
    private long lastStartTime = 0;
    private long totalTime = 0;
    @RecordActivity.Status
    private volatile int status;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void setViews() {
        progressBar.setMax(Constants.AUDIO_MAX_DURATION);

        initRecorder();

        recordBtn.setOnClickListener(this::btnClicked);
    }

    private void initRecorder() {
        File cache = FileUtil.getAudioCacheFile(this);
        if (cache.exists()) cache.delete();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(cache.getPath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOnInfoListener(this);
        mRecorder.setMaxDuration(Constants.AUDIO_MAX_DURATION * 1000);

        try {
            mRecorder.prepare();
            status = STATUS_PREPARED;
        } catch (IOException e) {
            e.printStackTrace();
            status = STATUS_ERROR;
        }
    }

    @UiThread(delay = 100)
    void updateProgress(boolean turn) {
        if (status == STATUS_RECORDING) {
            if (turn) {
                //TODO show progress bar
                progressBar.setVisibility(View.VISIBLE);
            }
            int d = (int) (SystemClock.elapsedRealtime() - lastStartTime) / 1000;
            if (d < Constants.AUDIO_MAX_DURATION) {
                progressBar.setProgress(d);
                updateProgress(false);
            } else {
                updateProgress(turn);
            }
        } else if (status == STATUS_RECORDED && turn && progressBar.getVisibility() == View.VISIBLE) {
            //TODO hide progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    void btnClicked(View view) {
        switch (status) {
            case UIStatus.STATUS_NOTHING:

                break;
            case STATUS_PREPARED:
                mRecorder.start();
                status = STATUS_RECORDING;
                lastStartTime = SystemClock.elapsedRealtime();
                updateProgress(true);
                break;
            case STATUS_RECORDING:
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                updateProgress(true);
                status = STATUS_RECORDED;
                onRecorded();
                break;
            case STATUS_RECORDED:
                mMediaPlayer = new MediaPlayer();
                try {
                    mMediaPlayer.setDataSource(FileUtil.getAudioCacheFile(this).getPath());
                    mMediaPlayer.setOnCompletionListener(mp -> status = STATUS_PLAYED);
                    status = STATUS_PLAYING;
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    status = STATUS_ERROR;
                    e.printStackTrace();
                }
                break;
            case STATUS_PLAYING:
                mMediaPlayer.pause();
                status = STATUS_PAUSE;
                break;
            case STATUS_PAUSE:
                mMediaPlayer.start();
                status = STATUS_PLAYING;
                break;
            case STATUS_PLAYED:
                mMediaPlayer.seekTo(0);
                status = STATUS_PAUSE;
                mMediaPlayer.start();
                status = STATUS_PLAYING;
                break;
            case STATUS_ERROR:

                break;
        }
    }

    private void onRecorded() {
        AnimationUtil.alphaShow(redoBtn);
        AnimationUtil.alphaShow(finishBtn);
    }

    @Click
    void redoBtnClicked(View view) {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            recordBtn.setImageResource(R.drawable.ic_capture_begin);
        }

        initRecorder();


        AnimationUtil.alphaHide(redoBtn);
        AnimationUtil.alphaHide(finishBtn);
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch (what) {
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
            case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
                if (status == STATUS_RECORDING) {
                    status = STATUS_RECORDED;
                    onRecorded();
                }
                break;
            case MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN:
                status = STATUS_ERROR;
                break;
        }
        updateProgress(true);
    }

    @IntDef({UIStatus.STATUS_NOTHING,
            STATUS_PREPARED,
            STATUS_RECORDING,
            STATUS_RECORDED,
            STATUS_PLAYING,
            STATUS_PAUSE,
            STATUS_PLAYED,
            STATUS_ERROR})
    private @interface Status {
    }
}

