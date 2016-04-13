package co.yishun.lighting.ui;

import android.net.Uri;
import android.view.WindowManager;
import android.widget.VideoView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import co.yishun.lighting.R;
import co.yishun.lighting.ui.common.BaseActivity;

/**
 * Created by carlos on 4/12/16.
 */
@EActivity(R.layout.activity_play)
public class PlayActivity extends BaseActivity {

    private static final String TAG = "PlayActivity";
    @ViewById
    VideoView videoView;
    @Extra
    Uri uri;

    @AfterViews
    void setupVideoPlayView() {
        videoView.setVideoURI(uri);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        videoView.start();
    }

    @Click(R.id.videoView)
    void videoClick() {
        if (videoView.isPlaying()) {
            videoView.pause();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            videoView.start();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public String getPageInfo() {
        return TAG;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

}
