package com.ldfs.theme.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldfs.theme.R;
import com.ldfs.theme.annotation.ID;

import java.util.ArrayList;

/**
 * Created by cz on 15/10/16.
 */
public class TextAdapter extends MyBaseAdapter<String> {

    public TextAdapter(Context context, ArrayList<String> strings) {
        super(context, strings);
    }

    @Override
    public View newView(int type, int position, View convertView, ViewGroup parent) {
        return initConvertView(parent, R.layout.list_item, new ViewHolder());
    }

    @Override
    public void initView(int type, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.textView.setText(getItem(position));
    }

    public static class ViewHolder {
        @ID(id = R.id.tv_text)
        TextView textView;
    }
}
