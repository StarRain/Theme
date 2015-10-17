package com.ldfs.theme.theme.bean;

/**
 * Created by cz on 15/10/15.
 */
public class ViewLayout extends StyleElement {
    public String layout;
    public int id;

    public ViewLayout() {
    }

    public ViewLayout(String layout, int id) {
        this.layout = layout;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewLayout r = (ViewLayout) o;
        return id == r.id && layout.equals(r.layout);
    }
}
