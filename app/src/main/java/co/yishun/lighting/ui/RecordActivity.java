package co.yishun.lighting.ui;

import android.media.MediaRecorder;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;

import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.ui.common.BaseActivity;
import co.yishun.lighting.ui.view.PressButton;
import co.yishun.lighting.util.FileUtil;
import co.yishun.lighting.util.LogUtil;

/**
 * Created by Carlos on 2016/4/10.
 */
@EActivity(R.layout.activity_record)
public class RecordActivity extends BaseActivity implements PressButton.OnPressListener, AudioRecorder.OnStartListener, AudioRecorder.OnPauseListener {
    private static final String TAG = "RecordActivity";

    @Extra
    String questionName;
    @ViewById
    PressButton recordBtn;
    @ViewById
    ProgressBar progressBar;
    AudioRecorder mRecorder;
    private long lastStartTime = 0;
    private long totalTime = 0;

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @AfterViews
    void setViews() {
        mRecorder = AudioRecorderBuilder.with(this)
                .fileName(FileUtil.getAudioCacheFile(this).getPath())
                .loggable()
                .config(new AudioRecorder.MediaRecorderConfig(
                        64 * 1024,
                        2,
                        MediaRecorder.AudioSource.MIC,
                        MediaRecorder.AudioEncoder.AAC))
                .build();


        recordBtn.setOnPressListener(this);
    }

    @Override
    public void onPressStart(View view) {
        LogUtil.d(TAG, "press start");

        mRecorder.start(this);
    }

    @Override
    public void onPressEnd(View view) {
        LogUtil.d(TAG, "press end");
        mRecorder.pause(this);
    }

    @Override
    public void onStarted() {
        lastStartTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onPaused(String activeRecordFileName) {
        long last = lastStartTime;
        lastStartTime = 0;
        totalTime += SystemClock.elapsedRealtime() - last;
    }
}

