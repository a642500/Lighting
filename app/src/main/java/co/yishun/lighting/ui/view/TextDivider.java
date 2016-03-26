package co.yishun.lighting.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by carlos on 3/26/16.
 */
public class TextDivider extends View {
    @ColorInt
    private int mDividerLineColor;
    private int mDividerTextColor;
    private int mDividerLineSize;
    private int mTextPadding;
    private String mDividerText;
    private Paint mLinePaint;
    private TextPaint mTextPaint;


    public TextDivider(Context context) {
        super(context);
    }

    public TextDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextDivider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {


        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTextPaint.setColor(mDividerTextColor);

        if (mDividerText != null) {
            canvas.drawText(mDividerText, 0, 0, mTextPaint);
        }

        mLinePaint.setColor(mDividerLineColor);
        
    }
}
