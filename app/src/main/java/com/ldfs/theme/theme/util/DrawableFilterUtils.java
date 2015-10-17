package com.ldfs.theme.theme.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cz on 15/8/9.
 */
public class DrawableFilterUtils {
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    @IntDef(value = {LEFT, TOP, RIGHT, BOTTOM})
    public @interface Duration {
    }

    /**
     * 设置图片颜色
     *
     * @param drawable
     * @param color
     */
    public static void setDrawableFilter(Drawable drawable, int color) {
        if (null != drawable) {
            drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
    }

    /**
     * 设置view的背影颜色过滤
     *
     * @param view
     * @param color
     */
    public static void setBackGroundFilter(View view, int color) {
        if (null != view) {
            setDrawableFilter(view.getBackground(), color);
        }
    }

    /**
     * 设置imageview Drawable颜色过滤
     *
     * @param imageView
     * @param color
     */
    public static void setImageDrawableFilter(ImageView imageView, int color) {
        if (null != imageView) {
            setDrawableFilter(imageView.getDrawable(), color);
        }
    }

    /**
     * 设置textview CompoundDrawable 颜色过滤
     * @param textView
     * @param duration
     * @param color
     */
    public static void setCompoundDrawableFilter(TextView textView, @Duration int duration, int color) {
        Drawable[] drawables = textView.getCompoundDrawables();
        if (null != textView) {
            setDrawableFilter(drawables[duration], color);
        }
    }

}
