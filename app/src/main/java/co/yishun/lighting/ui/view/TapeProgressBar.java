package co.yishun.lighting.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

/**
 * Created by Carlos on 2016/4/10.
 */
@SuppressWarnings("unused")
@CoordinatorLayout.DefaultBehavior(TapeProgressBar.TapeBehavior.class)
public class TapeProgressBar extends ProgressBar {
    public TapeProgressBar(Context context) {
        super(context);
    }

    public TapeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TapeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TapeProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*
    顶部到顶部是0.653
底部到底部是0.318
两边倒两边是0.173
     */

    public static class TapeBehavior extends CoordinatorLayout.Behavior<TapeProgressBar> {
        private Rect mRect = new Rect();
        private Rect mOut = new Rect();

        @Override
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, TapeProgressBar child, int layoutDirection) {

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final Rect rect = mRect;
            final Rect out = mOut;

            rect.set(coordinatorLayout.getPaddingLeft() + lp.leftMargin,
                    coordinatorLayout.getPaddingTop() + lp.topMargin,
                    coordinatorLayout.getWidth() - coordinatorLayout.getPaddingRight() - lp.rightMargin,
                    coordinatorLayout.getHeight() - coordinatorLayout.getPaddingBottom() - lp.bottomMargin);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            float cut = rect.width() * 0.173f / 2;
            rect.inset((int) cut, 0);
            float cutTop = rect.height() * 0.653f;
            rect.top += cutTop;
            float cutBottom = rect.height() * 0.318f;
            rect.bottom -= cutBottom;

            GravityCompat.apply(Gravity.TOP | Gravity.CENTER_VERTICAL, childWidth, childHeight, rect, out, layoutDirection);
            out.inset((int) cut, 0);

            child.layout(out.left, out.top, out.right, out.bottom);

            return true;
        }
    }
}
