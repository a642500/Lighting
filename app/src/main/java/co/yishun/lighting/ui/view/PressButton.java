package co.yishun.lighting.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Carlos on 2016/4/10.
 */
public class PressButton extends Button {
    private boolean pressed = false;
    private OnPressListener mListener;

    public PressButton(Context context) {
        super(context);
    }

    public PressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PressButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isEnabled() && !pressed) {
                    pressed = true;
                    onPressStart(this);
                    return true;
                }
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (pressed) {
                    pressed = false;
                    onPressEnd(this);
                }
                return true;
        }
        return false;
    }

    private void onPressStart(View view) {
        if (mListener != null) {
            mListener.onPressStart(view);
        }
    }

    private void onPressEnd(View view) {
        if (mListener != null) {
            mListener.onPressEnd(view);
        }
    }

    public void setOnPressListener(OnPressListener listener) {
        mListener = listener;
    }

    public interface OnPressListener {
        void onPressStart(View view);

        void onPressEnd(View view);
    }

}
