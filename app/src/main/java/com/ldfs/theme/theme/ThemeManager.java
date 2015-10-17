package com.ldfs.theme.theme;

import com.ldfs.theme.provider.BusProvider;
import com.ldfs.theme.theme.bean.DrawableFilterAttrItem;
import com.ldfs.theme.theme.bean.LayoutMapped;
import com.ldfs.theme.theme.bean.ListItem;
import com.ldfs.theme.theme.bean.Style;
import com.ldfs.theme.theme.bean.StyleElement;
import com.ldfs.theme.theme.bean.ViewLayout;
import com.ldfs.theme.theme.config.ConfigName;
import com.ldfs.theme.theme.config.Preference;
import com.ldfs.theme.theme.event.ThemeChangeEvent;
import com.ldfs.theme.theme.event.ThemeInitCompleteEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cz on 15/9/11.
 * 主题管理对象
 */
public class ThemeManager {
    private static final ThemeManager mConfigManager = new ThemeManager();
    private final HashMap<String, ArrayList<StyleElement>> mStyleElements;
    private final HashMap<String, HashMap<String, Style.AttrItem>> mStyleAttrs;
    private final HashMap<String, ArrayList<ListItem>> mLayoutListItems;
    private final HashMap<Integer, String> mLayoutItems;
    private final HashMap<String, ArrayList<ViewLayout>> mViewLayouts;
    private final HashMap<String, LayoutMapped> mLayoutMappeds;

    private final HashMap<String, HashMap<String, DrawableFilterAttrItem.FilterAttrItem>> mFilterAttrs;
    private String mTheme;//当前使用主题

    private ThemeManager() {
        mStyleElements = new HashMap<>();
        mStyleAttrs = new HashMap<>();
        mLayoutMappeds = new HashMap<>();
        mLayoutListItems = new HashMap<>();
        mLayoutItems = new HashMap<>();
        mFilterAttrs = new HashMap<>();
        mViewLayouts = new HashMap<>();
        mTheme = Preference.getString(ConfigName.THEME);//当前使用主题
    }

    public static final ThemeManager get() {
        return mConfigManager;
    }

    /**
     * 主题数据是否初始化
     *
     * @return
     */
    public boolean isInit() {
        return !mStyleAttrs.isEmpty() && !mStyleElements.isEmpty() && !mLayoutMappeds.isEmpty() && !mFilterAttrs.isEmpty();
    }

    /**
     * 初始化主题元素
     * 调用时机,主题元素初始化完,或者正常使用时,初始进入时
     */
    public void initThemeData(HashMap<String, Style> styleAttrs,
                              HashMap<String, ArrayList<StyleElement>> styleElements,
                              HashMap<String, LayoutMapped> layoutMappeds,
                              HashMap<String, ArrayList<ListItem>> listItems,
                              HashMap<String, DrawableFilterAttrItem> items,
                              HashMap<String, ArrayList<ViewLayout>> viewLayouts) {
        if (null != styleAttrs && !styleAttrs.isEmpty()) {
            //初始化HashMap<String,Style>->HashMap<String, ArrayList<StyleElement>>方便读取引用
            for (Map.Entry<String, Style> entry : styleAttrs.entrySet()) {
                String key = entry.getKey();
                Style value = entry.getValue();
                ArrayList<Style.AttrItem> attrItems = value.items;
                if (!attrItems.isEmpty()) {
                    int size = attrItems.size();
                    HashMap<String, Style.AttrItem> attrItemMaps = new HashMap<>(size);
                    mStyleAttrs.put(key, attrItemMaps);
                    for (int i = 0; i < size; i++) {
                        Style.AttrItem attrItem = attrItems.get(i);
                        attrItemMaps.put(attrItem.attr, attrItem);
                    }
                }
            }
        }
        if (null != styleElements && !styleElements.isEmpty()) {
            mStyleElements.putAll(styleElements);
        }
        if (null != layoutMappeds && !layoutMappeds.isEmpty()) {
            mLayoutMappeds.putAll(layoutMappeds);
            if (!mLayoutMappeds.isEmpty()) {
                for (Map.Entry<String, LayoutMapped> mapped : mLayoutMappeds.entrySet()) {
                    LayoutMapped value = mapped.getValue();
                    ArrayList<LayoutMapped.Item> layouts = value.layout;
                    if (!layouts.isEmpty()) {
                        for (int i = 0; i < layouts.size(); i++) {
                            LayoutMapped.Item item = layouts.get(i);
                            if (0 != item.id) {
                                mLayoutItems.put(item.id, item.layout);
                            }
                        }
                    }
                }
            }
        }
        if (null != listItems && !listItems.isEmpty()) {
            mLayoutListItems.putAll(listItems);
        }
        if (null != items && !items.isEmpty()) {
            for (Map.Entry<String, DrawableFilterAttrItem> entry : items.entrySet()) {
                DrawableFilterAttrItem value = entry.getValue();
                HashMap<String, DrawableFilterAttrItem.FilterAttrItem> attrItems = new HashMap<>();
                mFilterAttrs.put(entry.getKey(), attrItems);
                ArrayList<DrawableFilterAttrItem.FilterAttrItem> drawableFilterItems = value.items;
                if (null != drawableFilterItems && !drawableFilterItems.isEmpty()) {
                    for (int i = 0, length = drawableFilterItems.size(); i < length; i++) {
                        DrawableFilterAttrItem.FilterAttrItem filterAttrItem = drawableFilterItems.get(i);
                        attrItems.put(filterAttrItem.attr, filterAttrItem);
                    }
                }
            }
        }
        if (null != viewLayouts) {
            mViewLayouts.putAll(viewLayouts);
        }
        //发送初始化数据成功事件
        BusProvider.post(new ThemeInitCompleteEvent());
    }


    /**
     * 获得指定引用的引用值
     *
     * @param name
     * @return
     */
    public Style.AttrItem getStyleAttr(String name) {
        Style.AttrItem item = null;
        HashMap<String, Style.AttrItem> attrItems = mStyleAttrs.get(mTheme);
        if (null != attrItems) {
            item = attrItems.get(name);
        }
        return item;
    }

    /**
     * 获得布局对象映射
     *
     * @param name
     * @return
     */
    public ArrayList<StyleElement> getLayoutElements(String name) {
        return mStyleElements.get(name);
    }

    /**
     * 获得布局映射对象
     *
     * @param layout
     * @return
     */
    public LayoutMapped getLayoutMapped(String layout) {
        return mLayoutMappeds.get(layout);
    }

    /**
     * 获得drawableFilter过滤颜色
     *
     * @param name
     * @return
     */
    public DrawableFilterAttrItem.FilterAttrItem getFilterMapped(String name) {
        return mFilterAttrs.get(mTheme).get(name);
    }

    /**
     * 获得列表条目
     *
     * @param name
     * @return
     */
    public ArrayList<ListItem> getListItems(String name) {
        return mLayoutListItems.get(name);
    }

    /**
     * 根据控件id找到对应的设定布局对象,主要用于ListView以及RecyclerView等复用控件集
     *
     * @param name
     * @return
     */
    public String getLayoutById(int name) {
        return mLayoutItems.get(name);
    }

    /**
     * 获得布局内自定义控件
     *
     * @param name
     * @return
     */
    public ArrayList<ViewLayout> getViewLayout(String name) {
        return mViewLayouts.get(name);
    }

    /**
     * 设置主题样式
     */
    public void setTheme(String theme) {
        mTheme = theme;
        Preference.setString(ConfigName.THEME, theme);
        BusProvider.post(new ThemeChangeEvent());
    }

    public void toggle() {
        setTheme(ThemeConstants.LIGHT_STYLE.equals(mTheme) ? ThemeConstants.NIGHT_STYLE : ThemeConstants.LIGHT_STYLE);
    }

}
