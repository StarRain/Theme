package com.ldfs.theme.theme.bean;

import java.util.ArrayList;

/**
 * Created by cz on 15/8/8.
 * 主题元素
 */
public class StyleElement  {

    public String id;
    public String view;
    public String attrs;
    public String types;
    public String layout;
    public ArrayList<Element> items;

    public StyleElement() {
        items = new ArrayList<>();
    }

    public StyleElement(String id) {
        this.id = id;
        items = new ArrayList<>();
    }

    public StyleElement(String id, String attrs, String types, String layout) {
        this.id = id;
        this.attrs = attrs;
        this.types = types;
        this.layout = layout;
        items = new ArrayList<>();
    }



    @Override
    public String toString() {
        return view + " " + id + " " + items.size();
    }

    public static class Element {
        public ResType type;
        public String resType;
        public String value;

        public Element() {
        }

        public Element(ResType type, String resType, String value) {
            this.type = type;
            this.resType = resType;
            this.value = value;
        }
    }
}
