package com.ldfs.theme.theme;

import android.support.annotation.StringDef;
import java.util.ArrayList;

/**
 * Created by cz on 2015/10/16 06时26分.
 */
public class ThemeConstants{
	public static final String LIGHT_STYLE = "Light_Style";
	public static final String NIGHT_STYLE = "Night_Style";
	public static final ArrayList<String> THEMES;

	static {
		THEMES = new ArrayList<>();
		THEMES.add(LIGHT_STYLE);
		THEMES.add(NIGHT_STYLE);
	}
}
