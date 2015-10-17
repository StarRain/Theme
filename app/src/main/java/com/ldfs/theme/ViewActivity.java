package com.ldfs.theme;

import android.app.Activity;
import android.os.Bundle;

import com.ldfs.theme.annotation.ID;
import com.ldfs.theme.annotation.util.ViewHelper;
import com.ldfs.theme.provider.BusProvider;
import com.ldfs.theme.theme.ThemeConstants;
import com.ldfs.theme.theme.ThemeManager;
import com.ldfs.theme.theme.config.ConfigName;
import com.ldfs.theme.theme.config.Preference;
import com.ldfs.theme.theme.util.ThemeUtils;
import com.ldfs.theme.widget.FrameView;
import com.ldfs.theme.widget.SwitchView;

/**
 * Created by cz on 15/10/16.
 */
public class ViewActivity extends Activity {
    @ID(id = R.id.fv_frame)
    private FrameView mFrameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        BusProvider.regist(this);
        ViewHelper.init(this);
        SwitchView switchView = (SwitchView) findViewById(R.id.sv_theme_mode);
        String theme = Preference.getString(ConfigName.THEME);
        switchView.setChecked(ThemeConstants.NIGHT_STYLE.equals(theme), false);
        switchView.setOnCheckedChangeListener((v, isChecked) -> {
            ThemeManager.get().toggle();
            ThemeUtils.initThemeElement(this, null);
        });
        findViewById(R.id.ll_theme_mode).setOnClickListener(v -> switchView.toggle());

        findViewById(R.id.btn_load).setOnClickListener(v -> mFrameView.setProgressShown(true));
        findViewById(R.id.btn_empty).setOnClickListener(v -> mFrameView.setEmptyShown(true));
        findViewById(R.id.btn_error).setOnClickListener(v -> mFrameView.setRepeatShown(true));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.unregist(this);
    }
}
