package com.ldfs.theme;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;

import com.ldfs.theme.theme.util.ThemeUtils;

public class App extends Application {
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ThemeUtils.loadTheme();
    }


    public static Context getAppContext() {
        return context;
    }

    public static ContentResolver getAppResolver() {
        return context.getContentResolver();
    }

    public static Resources getAppResource() {
        return context.getResources();
    }

    public static String getStr(int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    public static int getResourcesColor(int id) {
        return context.getResources().getColor(id);
    }

    public static String[] getStringArray(int array) {
        return context.getResources().getStringArray(array);
    }

    public static float getDimension(int dimension) {
        return getAppResource().getDimension(dimension);
    }

    public static int[] getIntArray(int id) {
        return context.getResources().getIntArray(id);
    }

    public static int dip2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static ColorStateList getColorStateList(int res) {
        return context.getResources().getColorStateList(res);
    }


}
