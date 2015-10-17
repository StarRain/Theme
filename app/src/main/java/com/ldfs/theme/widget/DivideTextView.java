package com.ldfs.theme.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ldfs.theme.R;


/**
 * 分隔线布局
 *
 * @author momo
 * @Date 2014/8/8
 */
public class DivideTextView extends TextView {
    public final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    public static final int NONE = 0x00;
    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x08;

    private float mStrokeWidth;
    private int mDivideColor;
    private int mDividePadding;
    private int mGravity;
    private Paint mPaint;

    public DivideTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initAttribute(context, attrs);
    }

    public DivideTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DivideTextView(Context context) {
        this(context, null, 0);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DivideLinearLayout);
        setDivideGravity(a.getInt(R.styleable.DivideLinearLayout_dl_divideGravity, NONE));
        setStrokeWidth(a.getDimension(R.styleable.DivideLinearLayout_dl_divideWidth, resources.getDimension(R.dimen.line_height)));
        setDivideColor(a.getColor(R.styleable.DivideLinearLayout_dl_divideColor, resources.getColor(R.color.line)));
        setDividePadding((int) a.getDimension(R.styleable.DivideLinearLayout_dl_dividePadding, 0f));
        a.recycle();
    }


    public void setDivideGravity(int gravity) {
        this.mGravity = gravity;
        invalidate();
    }

    /**
     * 设置分隔线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        invalidate();
    }


    public void setDivideColor(int color) {
        this.mDivideColor = color;
        invalidate();
    }

    /**
     * 设置绘制线边距
     *
     * @param padding
     */
    public void setDividePadding(int padding) {
        this.mDividePadding = padding;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制周边分隔线
        drawDivide(canvas);
    }

    private void drawDivide(Canvas canvas) {
        drawDivide(canvas, mGravity == (mGravity | LEFT),
                mGravity == (mGravity | TOP),
                mGravity == (mGravity | RIGHT),
                mGravity == (mGravity | BOTTOM));
    }

    private void drawDivide(Canvas canvas, boolean drawLeft, boolean drawTop, boolean drawRight, boolean drawBottom) {
        mPaint.reset();
        mPaint.setColor(mDivideColor);
        int width = getWidth();
        int height = getHeight();
        float interval = mStrokeWidth / 2;
        mPaint.setStrokeWidth(mStrokeWidth);
        if (drawLeft) {
            canvas.drawLine(interval, mDividePadding, interval, height - mDividePadding, mPaint);
        }
        if (drawTop) {
            canvas.drawLine(mDividePadding, interval, width - mDividePadding, interval, mPaint);
        }
        if (drawRight) {
            canvas.drawLine(width - interval, mDividePadding, width - interval, height - mDividePadding, mPaint);
        }
        if (drawBottom) {
            canvas.drawLine(mDividePadding, height - interval, width - mDividePadding, height - interval, mPaint);
        }
    }
}
