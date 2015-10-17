package com.ldfs.theme.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.ldfs.theme.R;
import com.ldfs.theme.util.UnitUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * Created by momo on 2015/3/16.
 * 安卓自定义切换状态按钮
 */
public class SwitchView extends View implements View.OnClickListener {
    private float mStrokeWidth;//边缘线宽
    private int mStrokeColor;//边缘颜色
    private float mUncheckedWidth;//未选中时线宽
    private int mUncheckedColor;//未选中时颜色
    private int mFillColor;
    private int mCheckedColor;//选中时颜色
    private boolean isLeftPress;//左边按下状态
    private boolean isRightPress;//右边按下状态
    private boolean isChecked;
    private float mFraction;
    private float mPressFraction;//按下状态
    private Paint mPaint;
    private OnCheckedChangeListener mListener;

    public SwitchView(Context context) {
        this(context, null, 0);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        //外层外宽
        setStrokeWidth(a.getDimension(R.styleable.SwitchView_sv_strokeWidth, UnitUtils.dip2px(context, 2)));
        //外层线颜色
        setStrokeColor(a.getColor(R.styleable.SwitchView_sv_strokeColor, Color.GRAY));
        //内圆颜色
        setFillColor(a.getColor(R.styleable.SwitchView_sv_fillColor, Color.WHITE));
        //未选中时线颜色
        setUnCheckWidth(a.getDimension(R.styleable.SwitchView_sv_unCheckWidth, UnitUtils.dip2px(context, 2)));
        // 线宽
        setUnCheckColor(a.getColor(R.styleable.SwitchView_sv_unCheckColor, Color.GRAY));
        //选中时线颜色
        setCheckColor(a.getColor(R.styleable.SwitchView_sv_checkColor, Color.GREEN));
        a.recycle();
    }

    /**
     * 设置外围线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        invalidate();
    }

    public void setStrokeColor(int color) {
        this.mStrokeColor = color;
        invalidate();
    }

    public void setUnCheckWidth(float width) {
        this.mUncheckedWidth = width;
        invalidate();
    }

    public void setUnCheckColor(int color) {
        this.mUncheckedColor = color;
        invalidate();
    }

    public void setCheckColor(int color) {
        this.mCheckedColor = color;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isChecked) {
                    isLeftPress = true;
                    isRightPress = false;
                } else {
                    isLeftPress = false;
                    isRightPress = true;
                }
                getPressAnim(isLeftPress || isRightPress).start();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                setChecked(!isChecked);
                break;
            case MotionEvent.ACTION_CANCEL:
                getPressAnim(false).start();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        //绘外圆
        mPaint.setColor(evaluate(mStrokeColor, mCheckedColor));
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        //外层圆角矩阵
        canvas.drawRoundRect(new RectF(mStrokeWidth, mStrokeWidth, width - mStrokeWidth, height - mStrokeWidth), radius - mStrokeWidth, radius - mStrokeWidth, mPaint);

        //内层圆角矩阵
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(evaluate(mFillColor, mCheckedColor));
        float fractionWidth = ((width - mStrokeWidth * 2) / 2) * mFraction;
        float fractionHeight = ((height - mStrokeWidth * 2) / 2) * mFraction;
        canvas.drawRoundRect(new RectF(mStrokeWidth, mStrokeWidth, width - mStrokeWidth, height - mStrokeWidth), radius, radius, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(mStrokeWidth + fractionWidth, mStrokeWidth + fractionHeight, width - mStrokeWidth - fractionWidth, height - mStrokeWidth - fractionHeight), radius, radius, mPaint);

        mPaint.setColor(evaluate(mUncheckedColor, Color.WHITE));
        mPaint.setStrokeWidth(mUncheckedWidth);
        mPaint.setStyle(Paint.Style.FILL);
        float circlePadding = mStrokeWidth + mUncheckedWidth / 2;
        float pressSize = ((radius - circlePadding) / 2) * mPressFraction;
        canvas.drawRoundRect(new RectF(circlePadding - circlePadding * mFraction + width / 2 * mFraction + (isRightPress ? -pressSize : 0),
                circlePadding,
                width / 2 - circlePadding * mFraction + width / 2 * mFraction + (isLeftPress ? pressSize : 0),
                height - circlePadding), radius - circlePadding, radius - circlePadding, mPaint);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        //去掉用户自定义listener
        super.setOnClickListener(this);
    }


    /**
     * 开启按下动画
     *
     * @param isPress
     */
    private ValueAnimator getPressAnim(final boolean isPress) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                mPressFraction = isPress ? fraction : (1f - fraction);
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isPress) {
                    isLeftPress = isRightPress = false;
                }
            }
        });
        valueAnimator.setDuration(200);
        if (null != mListener) {
            mListener.onCheckedChanged(this, isChecked);
        }
        return valueAnimator;
    }


    @Override
    public void onClick(View view) {
//        setChecked(this.isChecked = !this.isChecked);
    }

    /**
     * 设置切换状态
     *
     * @param isChecked
     */
    public void setChecked(final boolean isChecked) {
        setChecked(isChecked, true);
    }

    public void setChecked(final boolean isChecked, boolean anim) {
        if (anim) {
            ValueAnimator pressAnim = getPressAnim(true);
            ValueAnimator checkedAnim = getCheckedAnim(isChecked, 200);
            ValueAnimator pressAnim1 = getPressAnim(false);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(pressAnim).before(checkedAnim).before(pressAnim1);
            animatorSet.start();
        } else {
            getCheckedAnim(isChecked, 0).start();
        }
    }

    private ValueAnimator getCheckedAnim(final boolean isChecked, long duration) {
        this.isChecked = isChecked;
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                mFraction = isChecked ? fraction : (1f - fraction);
                invalidate();
            }
        });
        valueAnimator.setDuration(duration);
        if (null != mListener) {
            mListener.onCheckedChanged(this, isChecked);
        }
        return valueAnimator;
    }

    public int evaluate(int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24);
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24);
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (mFraction * (endA - startA))) << 24) | ((startR + (int) (mFraction * (endR - startR))) << 16) | ((startG + (int) (mFraction * (endG - startG))) << 8)
                | ((startB + (int) (mFraction * (endB - startB))));
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.mListener = listener;
    }

    public void setFillColor(int mFillColor) {
        this.mFillColor = mFillColor;
        invalidate();
    }

    public void toggle() {
        setChecked(!isChecked);
    }

    /**
     * 是否选中
     *
     * @return
     */
    public boolean isChecked() {
        return isChecked;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchView switchView, boolean isChecked);
    }
}
