package com.ldfs.theme.theme.bean;


import java.util.ArrayList;

/**
 * Created by cz on 15/9/10.
 * 布局映射对象
 */
public class LayoutMapped {
    public String name;
    public ArrayList<Item> layout;
    public boolean enable;

    public LayoutMapped() {
        layout = new ArrayList<>();
    }

    public LayoutMapped(String name) {
        this(name, false);
    }

    public LayoutMapped(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
        this.layout = new ArrayList<>();
    }

    public static class Item {
        public String layout;
        public int id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LayoutMapped r = (LayoutMapped) o;
        return name.equals(r.name);
    }
}