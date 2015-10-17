package com.ldfs.theme.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.ldfs.theme.annotation.util.ViewHelper;
import com.ldfs.theme.theme.util.ThemeUtils;

import java.util.ArrayList;

/**
 * 简易的数据适配器
 *
 * @param <T>
 * @author Administrator
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter implements Filterable {
    protected LayoutInflater mInflater;
    protected ArrayList<T> ts;

    public MyBaseAdapter(Context context, ArrayList<T> ts) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.ts = new ArrayList<T>();
        if (null != ts && !ts.isEmpty()) {
            this.ts.addAll(ts);
        }
    }

    @Override
    public int getCount() {
        return ts.size();
    }

    @Override
    public T getItem(int position) {
        T t = null;
        try {
            t = ts.get(position);
        } catch (Exception e) {
        }
        return t;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (null == convertView) {
            convertView = newView(itemViewType, position, convertView, parent);
            ThemeUtils.initListItemView(convertView);
        }
        initView(itemViewType, position, convertView, parent);
        return convertView;
    }

    /**
     * 设置viewHolder
     *
     * @param layout
     * @param parent
     * @param holder
     * @param initParent
     * @return
     */
    protected View setViewHolder(int layout, ViewGroup parent, Object holder, boolean initParent) {
        View contentView = mInflater.inflate(layout, parent, false);
        ViewHelper.init(holder, contentView, initParent);
        contentView.setTag(holder);
        return contentView;
    }

    protected View setViewHolder(int layout, ViewGroup parent, Object holder) {
        return setViewHolder(layout, parent, holder, false);
    }

    public T remove(int position) {
        T t = this.ts.remove(position);
        notifyDataSetChanged();
        return t;
    }

    public T remove(T t) {
        T item = null;
        int index = this.ts.indexOf(t);
        if (-1 != index) {
            remove(index);
        }
        return item;
    }

    public void addHeaderData(ArrayList<T> ts) {
        if (null != ts && !ts.isEmpty()) {
            addDatas(0, ts);
        }
    }

    public void addFootData(ArrayList<T> ts) {
        if (null != ts && !ts.isEmpty()) {
            this.ts.addAll(ts);
            notifyDataSetChanged();
        }
    }

    public void swrpDatas(ArrayList<T> datas) {
        if (null != ts) {
            ts.clear();
        }
        ts.addAll(datas);
        notifyDataSetChanged();
    }


    /**
     * 添加一个对象体,允许为null
     *
     * @param t
     */
    public void addData(T t) {
        addData(getCount(), t);
    }


    /**
     * 在指定位置插入一个数据
     *
     * @param index
     * @param t
     */
    public void addData(int index, T t) {
        if (0 <= index && index <= this.ts.size()) {
            this.ts.add(index, t);
            notifyDataSetChanged();
        }
    }

    /**
     * 在指定位置插入一批数据
     *
     * @param index
     * @param items
     */
    public void addDatas(int index, ArrayList<T> items) {
        if (0 <= index && index <= this.ts.size() && null != items) {
            this.ts.addAll(index, items);
            notifyDataSetChanged();
        }
    }

    /**
     * 是否包含条目
     *
     * @param t
     * @return
     */
    public boolean contains(T t) {
        return this.ts.contains(t);
    }

    /**
     * 更新指定数据,若不存在,则不存更新
     *
     * @param item
     */
    public void updateItem(T item) {
        int index = this.ts.indexOf(item);
        if (-1 != index) {
            this.ts.set(index, item);
            notifyDataSetChanged();
        }
    }

    /**
     * 置换数据
     *
     * @param position
     * @param swapPosition
     */
    public void swrpData(int position, int swapPosition) {
        ts.set(position, ts.set(swapPosition, ts.get(position)));
    }

    public void clear() {
        if (null != ts) {
            ts.clear();
        }
    }

    /**
     * 过滤关键字
     */
    public void filterItem(CharSequence charSequence) {
        Filter filter = getFilter();
        if (null == filter) {
            throw new NullPointerException("过滤器对象不能为空!请复写getFilter()方法~");
        }
        filter.filter(charSequence);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public ArrayList<T> getItems() {
        return ts;
    }

    protected View initConvertView(ViewGroup parent, int layout, Object ViewHolder, boolean initParent) {
        View convertView = mInflater.inflate(layout, parent, false);
        ViewHelper.init(ViewHolder, convertView, initParent);
        convertView.setTag(ViewHolder);
        return convertView;
    }

    protected View initConvertView(ViewGroup parent, int layout, Object holder) {
        return initConvertView(parent, layout, holder, false);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    /**
     * 初始化view
     *
     * @param position
     * @param convertView
     * @param parent
     */
    public abstract View newView(int type, int position, View convertView, ViewGroup parent);

    public abstract void initView(int type, int position, View convertView, ViewGroup parent);


}
