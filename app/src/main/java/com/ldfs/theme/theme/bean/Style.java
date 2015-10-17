package com.ldfs.theme.theme.bean;

import java.util.ArrayList;

/**
 * Created by cz on 15/8/9.
 * 样式集
 */
public class Style {
    public String name;
    public int value;
    public ArrayList<AttrItem> items;

    public Style() {
        this.items = new ArrayList<>();
    }

    public Style(String name, int value) {
        this.name = name;
        this.value = value;
        this.items = new ArrayList<>();
    }


    public static class AttrItem  {
        public String attr;
        public AttrType type;
        public String value;

        public AttrItem() {
        }

        public AttrItem(String attr, String value) {
            this.attr = attr;
            this.value = value;
        }

        public AttrItem(String attr, AttrType type, String value) {
            this.attr = attr;
            this.type = type;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AttrItem r = (AttrItem) o;
            return attr.equals(r.attr) && value.equals(r.value);
        }

        @Override
        public String toString() {
            return "attr:" + attr + " type:" + type + " value:" + value;
        }
    }
}
