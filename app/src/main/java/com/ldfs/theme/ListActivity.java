package com.ldfs.theme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.ldfs.theme.annotation.ID;
import com.ldfs.theme.annotation.util.ViewHelper;
import com.ldfs.theme.list.TextAdapter;
import com.ldfs.theme.theme.ThemeConstants;
import com.ldfs.theme.theme.ThemeManager;
import com.ldfs.theme.theme.config.ConfigName;
import com.ldfs.theme.theme.config.Preference;
import com.ldfs.theme.theme.util.ThemeUtils;
import com.ldfs.theme.widget.SwitchView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cz on 15/10/16.
 */
public class ListActivity extends Activity {
    @ID(id = R.id.lv_list)
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ViewHelper.init(this);

        SwitchView switchView = (SwitchView) findViewById(R.id.sv_theme_mode);
        findViewById(R.id.ll_theme_mode).setOnClickListener(v -> switchView.toggle());
        String theme = Preference.getString(ConfigName.THEME);
        switchView.setChecked(ThemeConstants.NIGHT_STYLE.equals(theme), false);
        switchView.setOnCheckedChangeListener((v, isChecked) -> {
            ThemeManager.get().toggle();
            ThemeUtils.initThemeElement(this, null);
        });
        ArrayList<String> items = new ArrayList<>(Arrays.asList("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"));
        mListView.setAdapter(new TextAdapter(this, items));
    }

}
