package com.ldfs.theme.theme.bean;

import java.util.ArrayList;

/**
 * Created by cz on 15/9/11.
 * drawable颜色过滤引用
 */
public class DrawableFilterAttrItem  {
    public String name;
    public String value;
    public ArrayList<FilterAttrItem> items;

    public DrawableFilterAttrItem() {
        items = new ArrayList<>();
    }

    public DrawableFilterAttrItem(String name, String value) {
        this.name = name;
        this.value = value;
        items = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrawableFilterAttrItem r = (DrawableFilterAttrItem) o;
        return name.equals(r.name) && value.equals(value);
    }

    public static class FilterAttrItem {
        public String attr;
        public String color;

        public FilterAttrItem() {
        }

        public FilterAttrItem(String attr) {
            this.attr = attr;
        }

        public FilterAttrItem(String attr, String color) {
            this.attr = attr;
            this.color = color;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FilterAttrItem r = (FilterAttrItem) o;
            return attr.equals(r.attr);
        }
    }
}