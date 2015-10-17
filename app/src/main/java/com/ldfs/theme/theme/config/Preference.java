package com.ldfs.theme.theme.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.ldfs.theme.App;


/**
 * Created by cz on 15/8/12.
 * 配置类
 */
public class Preference {
    // 配置项名称
    private static final String PREFERENCE_NAME = "config";
    private static final int DEFAULT_INT_VALUE = -1;
    private static final String DEFAULT_VALUE = null;

    private static SharedPreferences getPreference() {
        Context appContext = App.getAppContext();
        return appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_MULTI_PROCESS);
    }

    /**
     * 根据角标获取 默认值 -1;
     *
     * @param index
     * @return
     */
    public static int getInt(int index) {
        SharedPreferences preference = getPreference();
        return preference.getInt(String.valueOf(index), DEFAULT_INT_VALUE);
    }

    /**
     * 根据角标获取 默认值 -1;
     *
     * @param index
     * @return
     */
    public static long getLong(int index) {
        SharedPreferences preference = getPreference();
        return preference.getLong(String.valueOf(index), DEFAULT_INT_VALUE);
    }


    /**
     * 获得boolean值
     *
     * @param index
     * @return
     */
    public static boolean getBoolean(int index) {
        SharedPreferences preference = getPreference();
        return 1 == preference.getInt(String.valueOf(index), -1);
    }


    /*
     * @param index
     * @return
     */
    public static String getString(int index) {
        SharedPreferences preference = getPreference();
        return preference.getString(String.valueOf(index), DEFAULT_VALUE);
    }


    public static void setInt(int index, int value) {
        SharedPreferences preference = getPreference();
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(String.valueOf(index), value).commit();
    }

    public static void setLong(int index, long value) {
        SharedPreferences preference = getPreference();
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(String.valueOf(index), value).commit();
    }

    public static void setString(int index, String value) {
        SharedPreferences preference = getPreference();
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(String.valueOf(index), value).commit();
    }

    public static void setBoolean(int index, Boolean value) {
        SharedPreferences preference = getPreference();
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(String.valueOf(index), value ? 1 : -1).commit();
    }
}
