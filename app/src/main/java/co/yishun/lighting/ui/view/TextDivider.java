package co.yishun.lighting.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import co.yishun.lighting.R;

/**
 * Created by carlos on 3/26/16.
 */
public class TextDivider extends View {
    float midY;
    float textWidth = 0f;
    float lineLength;
    float start1 = 0f;
    float start2;
    float textBaseline;
    float end1;
    float end2;
    @ColorInt
    private int mDividerLineColor;
    private int mDividerTextColor;
    private int mDividerLineSize;
    private int mTextPadding;
    private int mTextSize;
    private String mDividerText;
    private Paint mLinePaint;
    private TextPaint mTextPaint;

    public TextDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextDivider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TextDivider);

        Resources resources = getResources();
        mDividerLineColor = array.getColor(R.styleable.TextDivider_td_lineColor, resources.getColor(R.color.colorAccent));
        mDividerTextColor = array.getColor(R.styleable.TextDivider_td_textColor, resources.getColor(R.color.colorAccent));
        mDividerLineSize = array.getDimensionPixelSize(R.styleable.TextDivider_td_lineSize, 1);
        mTextPadding = array.getDimensionPixelSize(R.styleable.TextDivider_td_textPadding, 2);
        mTextSize = array.getDimensionPixelSize(R.styleable.TextDivider_td_textSize, 12);
        if (array.hasValue(R.styleable.TextDivider_td_dividerText))
            mDividerText = array.getString(R.styleable.TextDivider_td_dividerText);


        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mDividerTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mDividerLineColor);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(mDividerLineSize);

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMinimumHeight((int) mTextPaint.getFontSpacing() + getPaddingTop() + getPaddingBottom());

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        // for height
        int minHeight = getSuggestedMinimumHeight();
        int result = minHeight;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = minHeight;
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        setMeasuredDimension(getMeasuredWidth(), result);


        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        // get text baseline
        if (mDividerText != null) {
            textWidth = mTextPaint.measureText(mDividerText);
        }
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        textBaseline = (h + mTextPaint.getFontSpacing()) / 2 - metrics.bottom;


        midY = h / 2;
        final float startX = getPaddingLeft();
        final float endX = w - getPaddingRight();
        lineLength = (endX - startX - mTextPadding * 2 - textWidth) / 2;

        start1 = startX;
        end1 = startX + lineLength;
        start2 = endX - lineLength;
        end2 = endX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDividerText != null) {
            canvas.drawText(mDividerText, getMeasuredWidth() / 2, textBaseline, mTextPaint);
        }

        canvas.drawLine(start1, midY, end1, midY, mLinePaint);
        canvas.drawLine(start2, midY, end2, midY, mLinePaint);
    }
}
