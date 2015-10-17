package com.ldfs.theme.theme.bean;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by cz on 15/8/8.
 * 引用类型
 */
public class ResType {
    private static final ArrayList<String> ATTR_TYPES;

    static {
        ATTR_TYPES = new ArrayList<>();
        Type[] values = Type.values();
        for (int i = 0, length = values.length; i < length; i++) {
            ATTR_TYPES.add(values[i].toString());
        }
    }

    public String value;
    public Type type;

    public static ResType getType(String name) {
        ResType resType = new ResType();
        int i = ATTR_TYPES.indexOf(name);
        if (-1 != i) {
            resType.type = Type.valueOf(name);
            resType.value = resType.type.toString();
        } else {
            if (name.endsWith("Filter")) {
                resType.type = Type.valueOf("otherFilter");
            } else {
                resType.type = Type.valueOf("other");
            }
            int index = name.indexOf("_");
            if (-1 != index) {
                //自定义属性
                String attrName = name.substring(index + 1);
                name = "set" + attrName.substring(0, 1).toUpperCase(Locale.CHINA) + attrName.subSequence(1, attrName.length());
            }
            resType.value = name;
        }
        return resType;
    }


    public enum Type {
        setImageResource,
        setImageResourceFilter,
        setBackgroundResource,
        setBackgroundResourceFilter,
        setTextColor,
        setHintTextColor,
        leftDrawable,
        leftDrawableFilter,
        topDrawable,
        topDrawableFilter,
        rightDrawable,
        rightDrawableFilter,
        bottomDrawable,
        bottomDrawableFilter,
        otherFilter,
        other
    }
}
