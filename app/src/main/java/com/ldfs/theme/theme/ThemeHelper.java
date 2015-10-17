package com.ldfs.theme.theme;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldfs.theme.App;
import com.ldfs.theme.theme.bean.AttrType;
import com.ldfs.theme.theme.bean.DrawableFilterAttrItem;
import com.ldfs.theme.theme.bean.ResType;
import com.ldfs.theme.theme.bean.Style;
import com.ldfs.theme.theme.bean.StyleElement;
import com.ldfs.theme.theme.util.DrawableFilterUtils;
import com.ldfs.theme.theme.util.ResUtils;

import java.lang.reflect.Method;

/**
 * Created by cz on 15/8/9.
 * 主是元素操作类
 */
public class ThemeHelper {
    private static final String TAG = ThemeHelper.class.getSimpleName();

    /**
     * 设置控件样式
     *
     * @param view 当前控件
     * @param item 资源引用
     */
    public static void setStyle(View view, StyleElement.Element item, Style.AttrItem styleAttr) {
        ResType resType = item.type;
        switch (resType.type) {
            case setImageResource:
            case setBackgroundResource:
                int res = getStyleValue(styleAttr);
                invokeMethod(view, resType.value, int.class, res);
                break;
            case setTextColor:
            case setHintTextColor:
                setTextColor(view, styleAttr, resType);
                break;
            case leftDrawable:
                setTextDrawable(view, DrawableFilterUtils.LEFT, ResUtils.drawable(styleAttr.value));
                break;
            case topDrawable:
                setTextDrawable(view, DrawableFilterUtils.TOP, ResUtils.drawable(styleAttr.value));
                break;
            case rightDrawable:
                setTextDrawable(view, DrawableFilterUtils.RIGHT, ResUtils.drawable(styleAttr.value));
                break;
            case bottomDrawable:
                setTextDrawable(view, DrawableFilterUtils.BOTTOM, ResUtils.drawable(styleAttr.value));
                break;
            case setImageResourceFilter:
                setImageDrawableFilter(view, item.resType);
                break;
            case setBackgroundResourceFilter:
                setBackgroundDrawableFilter(view, item.resType);
                break;
            case leftDrawableFilter:
                setTextDrawableFilter(view, DrawableFilterUtils.LEFT, item.resType);
                break;
            case topDrawableFilter:
                setTextDrawableFilter(view, DrawableFilterUtils.TOP, item.resType);
                break;
            case rightDrawableFilter:
                setTextDrawableFilter(view, DrawableFilterUtils.RIGHT, item.resType);
                break;
            case bottomDrawableFilter:
                setTextDrawableFilter(view, DrawableFilterUtils.BOTTOM, item.resType);
                break;
            case otherFilter:
                setCustomViewAttrFilter(view, resType.value, styleAttr);
                break;
            case other:
                setCustomViewAttr(view, resType.value, styleAttr);
                break;
        }

    }

    private static int getStyleValue(Style.AttrItem styleAttr) {
        int res;
        switch (styleAttr.type) {
            case color:
                res = ResUtils.color(styleAttr.value);
                break;
            case drawable:
            default:
                res = ResUtils.drawable(styleAttr.value);
                break;
        }
        return res;
    }

    /**
     * 设置文字颜色
     *
     * @param view
     * @param styleAttr
     * @param resType
     */
    private static void setTextColor(View view, Style.AttrItem styleAttr, ResType resType) {
        String value = styleAttr.value;
        AttrType attrType = styleAttr.type;
        switch (attrType) {
            case color:
                invokeMethod(view, resType.value, int.class, getResourceColor(value));
                break;
            case drawable:
            default:
                invokeMethod(view, resType.value, ColorStateList.class, App.getColorStateList(ResUtils.drawable(value)));
                break;
        }
    }

    /**
     * 设置imageView的drawable颜色过滤
     *
     * @param view
     * @param attr
     */
    public static void setImageDrawableFilter(View view, String attr) {
        if (view instanceof ImageView) {
            DrawableFilterAttrItem.FilterAttrItem attrItem = ThemeManager.get().getFilterMapped(attr);
            if (null != attrItem) {
                //取引用,取drawableFilter对应颜色
                DrawableFilterUtils.setImageDrawableFilter((ImageView) view, getResourceColor(attrItem.color));
            }
        }
    }

    /**
     * 设置控件背景drawable颜色过滤
     *
     * @param view
     * @param attr
     */
    public static void setBackgroundDrawableFilter(View view, String attr) {
        DrawableFilterAttrItem.FilterAttrItem attrItem = ThemeManager.get().getFilterMapped(attr);
        if (null != attrItem) {
            DrawableFilterUtils.setBackGroundFilter(view, getResourceColor(attrItem.color));
        }
    }

    /**
     * 设置自定义属性drawable过滤
     *
     * @param view
     * @param type
     * @param styleAttr
     */
    private static void setCustomViewAttrFilter(View view, String type, Style.AttrItem styleAttr) {
        String value = styleAttr.value;
        AttrType attrType = styleAttr.type;
        if (AttrType.drawable == attrType) {
            DrawableFilterAttrItem.FilterAttrItem filterMapped = ThemeManager.get().getFilterMapped(value);
            invokeMethod(view, type, int.class, getResourceColor(filterMapped.color));
        }
    }

    /**
     * 设置自定义控件引用值
     *
     * @param view
     * @param type
     * @param styleAttr
     */
    private static void setCustomViewAttr(View view, String type, Style.AttrItem styleAttr) {
        String value = styleAttr.value;
        AttrType attrType = styleAttr.type;
        int resValue;
        switch (attrType) {
            case color:
                resValue = getResourceColor(value);
                break;
            case drawable:
            default:
                resValue = ResUtils.drawable(value);
                break;
        }
        invokeMethod(view, type, int.class, resValue);
    }

    private static void setTextDrawableFilter(View view, @DrawableFilterUtils.Duration int duration, String attrName) {
        DrawableFilterAttrItem.FilterAttrItem attrItem = ThemeManager.get().getFilterMapped(attrName);
        if (null != attrItem && !TextUtils.isEmpty(attrItem.color)) {
            setTextDrawableFilter(view, duration, getResourceColor(attrItem.color));
        }
    }

    /**
     * 设置textDrawable过滤
     *
     * @param view
     */
    public static void setTextDrawableFilter(View view, @DrawableFilterUtils.Duration int duration, int color) {
        if (!(view instanceof TextView)) return;
        try {
            ((TextView) view).getCompoundDrawables();
            Method method = view.getClass().getMethod("getCompoundDrawables");
            if (null != method) {
                Drawable[] drawables = (Drawable[]) method.invoke(view);
                Drawable drawable = drawables[duration];
                if (null != drawable) {
                    DrawableFilterUtils.setDrawableFilter(drawable, color);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param view
     * @param duration
     */
    private static void setTextDrawable(View view, @DrawableFilterUtils.Duration int duration, int res) {
        if (null == view || !(view instanceof TextView)) return;
        TextView textView = ((TextView) view);
        Drawable[] compoundDrawables = textView.getCompoundDrawables();
        Drawable drawable = getDrawable(res);
        if (null != drawable) {
            textView.setCompoundDrawablesWithIntrinsicBounds(DrawableFilterUtils.LEFT != duration ? compoundDrawables[0] : drawable,
                    DrawableFilterUtils.TOP != duration ? compoundDrawables[1] : drawable,
                    DrawableFilterUtils.RIGHT != duration ? compoundDrawables[2] : drawable,
                    DrawableFilterUtils.BOTTOM != duration ? compoundDrawables[3] : drawable);
        }
    }

    public static Drawable getDrawable(int res) {
        return App.getAppResource().getDrawable(res);
    }

    public static int getResourceColor(String res) {
        int color = -1;
        if (!TextUtils.isEmpty(res)) {
            color = App.getResourcesColor(ResUtils.color(res));
        }
        return color;
    }

    /**
     * @param view
     * @param methodName
     * @param res
     */
    private static void invokeMethod(View view, String methodName, Class<?> clazz, Object res) {
        try {
            Method method = view.getClass().getMethod(methodName, clazz);
            if (null != method) {
                method.invoke(view, res);
            }
        } catch (Exception e) {
            Log.e(TAG, "view:" + view + "\nmethodName:" + methodName + " res:" + res);
        }
    }
}
