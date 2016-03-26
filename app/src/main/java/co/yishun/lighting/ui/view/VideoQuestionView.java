package co.yishun.lighting.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by carlos on 3/24/16.
 */

@SuppressWarnings("unused")
public class VideoQuestionView extends LinearLayout {
    public VideoQuestionView(Context context) {
        super(context);
    }

    public VideoQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoQuestionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {

    }

    public void setVideo() {

        //TODO finish up
    }


}
