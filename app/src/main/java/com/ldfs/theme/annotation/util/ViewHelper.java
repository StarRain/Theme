package com.ldfs.theme.annotation.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ldfs.theme.anim.listener.ViewImageClickListener;
import com.ldfs.theme.annotation.ID;
import com.ldfs.theme.annotation.OnClick;
import com.ldfs.theme.annotation.ViewClick;
import com.ldfs.theme.theme.util.ThemeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 控件寻找id与设置点击事件帮助类
 *
 * @author 扑倒末末
 */
public class ViewHelper {

    /**
     * 初始化属性
     *
     * @param object
     */
    public static void init(Object object) {
        init(object, null);
    }

    /**
     * 初始化fragment
     */
    public static void init(Object object, View view) {
        init(object, view, false);
    }

    /**
     * 初始化Fragment/Dialog/View
     *
     * @param object
     * @param view
     * @param initParent 是否初始化父类-----使用于ViewHolder 对象继承
     */
    public static void init(Object object, View view, boolean initParent) {
        initView(object, view, initParent);
        initMethodClick(object, view);
        initClick(object, view);
        ThemeUtils.initThemeElement(object, view);
    }

    /**
     * 初始化方法点击事件
     *
     * @param object
     * @param view
     */
    private static void initMethodClick(final Object object, View view) {
        if (null != object) {
            final Method[] methods = object.getClass().getDeclaredMethods();
            int length = methods.length;
            for (int i = 0; i < length; i++) {
                methods[i].setAccessible(true);
                OnClick onClick = methods[i].getAnnotation(OnClick.class);
                if (null != onClick) {
                    int[] ids = onClick.ids();
                    if (null != ids) {
                        View findView = null;
                        for (int j = 0; j < ids.length; j++) {
                            if (object instanceof Activity) {
                                findView = ((Activity) object).findViewById(ids[j]);
                            } else if (object instanceof Dialog) {
                                findView = ((Dialog) object).findViewById(ids[j]);
                            } else if (object instanceof View) {
                                findView = ((View) object).findViewById(ids[j]);
                            } else if (null != view) {
                                findView = view.findViewById(ids[j]);
                            }
                            if (null != findView) {
                                // 设置方法点击
                                final int finalI = i;
                                findView.setOnClickListener(new ViewImageClickListener((View v) -> {
                                    try {
                                        methods[finalI].invoke(object, v);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化控件点击事件
     *
     * @param object
     */
    private static void initClick(Object object, View view) {
        if (null != object && object instanceof OnClickListener) {
            ViewClick viewClick = object.getClass().getAnnotation(ViewClick.class);
            if (null != viewClick && null != viewClick.ids() && 0 < viewClick.ids().length) {
                int[] ids = viewClick.ids();
                View findView = null;
                for (int i = 0; i < ids.length; i++) {
                    if (object instanceof Activity) {
                        findView = ((Activity) object).findViewById(ids[i]);
                    } else if (object instanceof Dialog) {
                        findView = ((Dialog) object).findViewById(ids[i]);
                    } else if (object instanceof View) {
                        findView = ((View) object).findViewById(ids[i]);
                    } else if (null != view) {
                        findView = view.findViewById(ids[i]);
                    } else {
                        throw new IllegalArgumentException("初始化点击失败:" + object);
                    }
                    if (null != findView) {
                        setOnClickListener(object, findView, viewClick.viewImage());
                    }
                }
            }
        }
    }

    /**
     * 查找控件id
     *
     * @param object
     * @param view
     * @param initParent
     */
    private static void initView(Object object, View view, boolean initParent) {
        initObject(object, object.getClass(), view);
        if (initParent) {
            initObject(object, object.getClass().getSuperclass(), view);
        }
    }

    private static void initObject(Object object, Class<?> clazz, View view) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            ID id = field.getAnnotation(ID.class);
            if (null != id) {
                // 设置控件id
                if (0 != id.id()) {
                    try {
                        View findView = null;
                        if (object instanceof Activity) {
                            // activity
                            findView = ((Activity) object).findViewById(id.id());
                        } else if (object instanceof Dialog) {
                            // 对话框---
                            findView = ((Dialog) object).findViewById(id.id());
                        } else if (object instanceof View) {
                            findView = ((View) object).findViewById(id.id());
                        } else if (null != view) {
                            // view---用于fragment
                            findView = view.findViewById(id.id());
                        } else {
                            throw new IllegalArgumentException("初始化view失败:" + field.getName());
                        }
                        if (null != findView) {
                            field.set(object, findView);
                            // 设置当前控件点击
                            if (id.click()) {
                                setOnClickListener(object, findView, id.viewImage());
                            }
                            // 设置子控件点击事件
                            if (id.childClick() && findView instanceof ViewGroup) {
                                for (int i = 0; i < ((ViewGroup) findView).getChildCount(); i++) {
                                    setOnClickListener(object, ((ViewGroup) findView).getChildAt(i), id.viewImage());
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 设置控件onClick事件到当前acivity----activity必须实现onclick接口！
     *
     * @param object
     * @param view
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void setOnClickListener(Object object, View view, boolean viewImage) {
        try {
            Method method = view.getClass().getMethod("setOnClickListener", OnClickListener.class);
            if (null != method && object instanceof OnClickListener) {
                OnClickListener listener = viewImage ? new ViewImageClickListener((OnClickListener) object) : ((OnClickListener) object);
                method.invoke(view, listener);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
