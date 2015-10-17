package com.ldfs.theme.theme.bean;

/**
 * Created by cz on 15/8/9.
 */
public enum AttrType {
    color("color"), drawable("drawable");
    public String value;

    AttrType(String value) {
        this.value = value;
    }

    public static AttrType getType(String type) {
        return valueOf(type);
    }
}
