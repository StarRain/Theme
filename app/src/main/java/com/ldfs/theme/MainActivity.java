package com.ldfs.theme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ldfs.theme.annotation.util.ViewHelper;
import com.ldfs.theme.provider.BusProvider;
import com.ldfs.theme.theme.ThemeConstants;
import com.ldfs.theme.theme.ThemeManager;
import com.ldfs.theme.theme.config.ConfigName;
import com.ldfs.theme.theme.config.Preference;
import com.ldfs.theme.theme.event.ThemeChangeEvent;
import com.ldfs.theme.theme.event.ThemeInitCompleteEvent;
import com.ldfs.theme.theme.util.ThemeUtils;
import com.ldfs.theme.widget.SwitchView;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.regist(this);
        setContentView(R.layout.activity_main);
        ViewHelper.init(this);
        SwitchView switchView = (SwitchView) findViewById(R.id.sv_theme_mode);
        String theme = Preference.getString(ConfigName.THEME);
        switchView.setChecked(ThemeConstants.NIGHT_STYLE.equals(theme), false);
        switchView.setOnCheckedChangeListener((v, isChecked) -> ThemeManager.get().toggle());
        findViewById(R.id.ll_theme_mode).setOnClickListener(v -> switchView.toggle());
        findViewById(R.id.tv_list).setOnClickListener(v -> startActivity(new Intent(this, ListActivity.class)));
        findViewById(R.id.tv_custom).setOnClickListener(v -> startActivity(new Intent(this, ViewActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.unregist(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 主题元素初始化完毕事件
     *
     * @param event
     */
    @Subscribe
    public void OnThemeInitCompleteEvent(ThemeInitCompleteEvent event) {
        ThemeUtils.initThemeElement(this, null);
    }

    /**
     * 主题更切事件
     *
     * @param event
     */
    @Subscribe
    public void OnThemeChangeEvent(ThemeChangeEvent event) {
        ThemeUtils.initThemeElement(this, null);
    }
}
