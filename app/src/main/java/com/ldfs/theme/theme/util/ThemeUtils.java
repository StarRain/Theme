package com.ldfs.theme.theme.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ldfs.theme.theme.ThemeHelper;
import com.ldfs.theme.theme.ThemeManager;
import com.ldfs.theme.theme.bean.DrawableFilterAttrItem;
import com.ldfs.theme.theme.bean.LayoutMapped;
import com.ldfs.theme.theme.bean.ListItem;
import com.ldfs.theme.theme.bean.Style;
import com.ldfs.theme.theme.bean.StyleElement;
import com.ldfs.theme.theme.bean.ViewLayout;
import com.ldfs.theme.theme.read.FilterMappedReader;
import com.ldfs.theme.theme.read.LayoutMappedReader;
import com.ldfs.theme.theme.read.ListItemReader;
import com.ldfs.theme.theme.read.ThemeLayoutReader;
import com.ldfs.theme.theme.read.ThemeStyleReader;
import com.ldfs.theme.theme.read.ViewLayoutReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cz on 15/9/10.
 * 主题工具类
 */
public class ThemeUtils {
    /**
     * 装载主题
     */
    public static void loadTheme() {
        if (ThemeManager.get().isInit()) return;
        new AsyncTask<Void, Void, HashMap<String, Style>>() {
            private HashMap<String, ArrayList<StyleElement>> styleElementMaps = new HashMap<>();
            private HashMap<String, LayoutMapped> layoutMappedItems = new HashMap<>();
            private HashMap<String, ArrayList<ListItem>> layoutListItems = new HashMap<>();
            private HashMap<String, ArrayList<ViewLayout>> viewLayouts = new HashMap<>();
            private HashMap<String, DrawableFilterAttrItem> filterAttrItems = new HashMap<>();

            @Override
            protected HashMap<String, Style> doInBackground(Void... params) {
                //主题操作元素集
                HashMap<String, ArrayList<StyleElement>> themeLayouts = new ThemeLayoutReader().read();
                if (null != themeLayouts) {
                    styleElementMaps.putAll(themeLayouts);
                }
                HashMap<String, Style> themeStyle = new ThemeStyleReader().read();
                //初始化布局映射引用
                HashMap<String, LayoutMapped> layoutMappeds = new LayoutMappedReader().read();
                if (null != layoutMappeds) {
                    layoutMappedItems.putAll(layoutMappeds);
                }
                //初始化布局列表引用
                HashMap<String, ArrayList<ListItem>> listItems = new ListItemReader().read();
                if (null != listItems) {
                    layoutListItems.putAll(listItems);
                }
                //初始化filter引用资源
                HashMap<String, DrawableFilterAttrItem> items = new FilterMappedReader().read();
                if (null != items) {
                    filterAttrItems.putAll(items);
                }
                //初始化自定义模板控件
                HashMap<String, ArrayList<ViewLayout>> layouts = new ViewLayoutReader().read();
                if (null != layouts) {
                    viewLayouts.putAll(layouts);
                }
                return themeStyle;
            }

            @Override
            protected void onPostExecute(HashMap<String, Style> styles) {
                super.onPostExecute(styles);
                //初始化主题元素
                ThemeManager.get().initThemeData(styles, styleElementMaps, layoutMappedItems, layoutListItems, filterAttrItems, viewLayouts);
            }
        }.execute();
    }


    /**
     * 设置主题样式
     *
     * @param object
     */
    public static void initThemeElement(Object object, View view) {
        if (null == object) return;
        String name = object.getClass().getSimpleName();
        ThemeManager themeManager = ThemeManager.get();
        LayoutMapped layoutMapped = themeManager.getLayoutMapped(name);
//        if (null != layoutMapped && layoutMapped.enable) {
        if (null != layoutMapped) {
            ArrayList<LayoutMapped.Item> layoutItem = layoutMapped.layout;
            if (!layoutItem.isEmpty()) {
                for (LayoutMapped.Item item : layoutItem) {
                    //初始化布局对象
                    initLayout(object, view, item.layout);
                    //初始化列表控件
                    initList(object, item.layout, view);
                    //初始化自定义模板控件
                    initCustomView(object, view);
                }
            }
        }
    }


    /**
     * 初始化布局
     *
     * @param object
     * @param view
     * @param layout
     */
    public static void initLayout(Object object, View view, String layout) {
        ThemeManager themeManager = ThemeManager.get();
        //获得当前界面主题操作元素
        ArrayList<StyleElement> layoutElements = themeManager.getLayoutElements(layout);
        if (null != layoutElements) {
            for (int i = 0, size = layoutElements.size(); i < size; i++) {
                StyleElement styleElement = layoutElements.get(i);
                View findView = findView(object, view, ResUtils.id(styleElement.id));
                if (null != findView) {
                    ArrayList<StyleElement.Element> items = styleElement.items;
                    //resType: drawable_white_item_select_filter type:other value:setBackgroundResourceFilter
                    if (null != items && !items.isEmpty()) {
                        for (StyleElement.Element item : items) {
                            Style.AttrItem styleAttr = themeManager.getStyleAttr(item.value);
                            if (null != styleAttr && !TextUtils.isEmpty(styleAttr.value)) {
                                ThemeHelper.setStyle(findView, item, styleAttr);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化列表控件集
     *
     * @param layout
     * @param view
     */
    public static void initList(Object object, String layout, View view) {
        ThemeManager themeManager = ThemeManager.get();
        //获得当前界面主题操作元素
        ArrayList<ListItem> listItems = themeManager.getListItems(layout);
        if (null != listItems) {
            for (int i = 0, size = listItems.size(); i < size; i++) {
                ListItem listItem = listItems.get(i);
                View findView = findView(object, view, listItem.id);
                if (null != findView) {
                    //在这里可以做所以自定义的ListView/RecyclerView还有其他的列表复用控件的自定义设置
                    /*if ("PullToRefreshListView".equals(listItem.name)) {
                        ListView refreshableView = ((PullToRefreshListView) findView).getRefreshableView();
                        initRecyclerBin(refreshableView);
                        initViewGroup(refreshableView);
                    } else */
                    if (findView instanceof AbsListView) {
                        initRecyclerBin((AbsListView) findView);
                        initViewGroup(findView);
                    } else {
                        initViewGroup(findView);
                    }
                }
            }
        }
    }

    private static void initRecyclerBin(AbsListView view) {
        try {
            Field localField = AbsListView.class.getDeclaredField("mRecycler");
            localField.setAccessible(true);
            Object recyclerBin = localField.get(view);
            Class<?> clazz = Class.forName("android.widget.AbsListView$RecycleBin");
            Field scrapField = clazz.getDeclaredField("mScrapViews");
            scrapField.setAccessible(true);
            ArrayList<View>[] scrapViews;
            scrapViews = (ArrayList<View>[]) scrapField.get(recyclerBin);
            if (null != scrapViews) {
                int length = scrapViews.length;
                for (int i = 0; i < length; i++) {
                    if (null != scrapViews[i] && !scrapViews[i].isEmpty()) {
                        for (int j = 0; j < scrapViews[i].size(); j++) {
                            initListItemView(scrapViews[i].get(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化一个view
     * 根据view的id,获取获对应布局操作体.进行初始化操作
     *
     * @param view
     */
    public static void initViewGroup(View view) {
        if (null != view && view instanceof ViewGroup) {
            ViewGroup findLayout = (ViewGroup) view;
            int childCount = findLayout.getChildCount();
            for (int j = 0; j < childCount; j++) {
                View childView = findLayout.getChildAt(j);
                initListItemView(childView);
            }
        }
    }

    /**
     * 初始化ListView内的条目view
     *
     * @param childView
     */
    public static void initListItemView(View childView) {
        int id = childView.getId();
        ThemeManager themeManager = ThemeManager.get();
        String layout = themeManager.getLayoutById(id);
        if (!TextUtils.isEmpty(layout)) {
            initLayout(null, childView, layout);
        }
    }

    /**
     * 初始化自定义模板控件
     *
     * @param object
     * @param view
     */
    public static void initCustomView(Object object, View view) {
        if (null == object) return;
        ThemeManager themeManager = ThemeManager.get();
        String name = object.getClass().getSimpleName();
        LayoutMapped layoutMapped = themeManager.getLayoutMapped(name);
        if (null != layoutMapped) {
            ArrayList<LayoutMapped.Item> layout = layoutMapped.layout;
            if (null != layout && !layout.isEmpty()) {
                for (int i = 0; i < layout.size(); i++) {
                    LayoutMapped.Item item = layout.get(i);
                    //获得当前界面主题操作元素
                    ArrayList<ViewLayout> viewLayouts = themeManager.getViewLayout(item.layout);
                    if (null != viewLayouts) {
                        for (int j = 0, size = viewLayouts.size(); j < size; j++) {
                            ViewLayout viewLayout = viewLayouts.get(j);
                            View findView = findView(object, view, viewLayout.id);
                            if (null != findView) {
                                //获得界面操作元素
                                initLayout(object, view, viewLayout.layout);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 查找view对象
     *
     * @param object
     * @param view
     */
    private static View findView(Object object, View view, int id) {
        View findView = null;
        if (null != object) {
            if (object instanceof Activity) {
                findView = ((Activity) object).findViewById(id);
            } else if (object instanceof Dialog) {
                findView = ((Dialog) object).findViewById(id);
            } else if (object instanceof View) {
                findView = ((View) object).findViewById(id);
            } else if (null != view) {
                findView = view.findViewById(id);
            }
        } else if (null != view) {
            findView = view.findViewById(id);
        }
        return findView;
    }
}
