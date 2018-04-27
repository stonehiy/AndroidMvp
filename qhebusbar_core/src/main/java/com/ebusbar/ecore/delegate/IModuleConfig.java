package com.ebusbar.ecore.delegate;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * 可以给框架配置一些参数,需要实现在 AndroidManifest 中声明该实现类
 */
public interface IModuleConfig {


    /**
     * 使用给框架配置一些配置参数
     *
     * @param context
     */
    void applyOptions(Context context);

    /**
     * 在Application的生命周期中注入一些操作
     *
     * @param context
     * @param appLifecycles
     */
    void injectAppLifecycle(Context context, List<AppLifecycles> appLifecycles);

    /**
     * }在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param activityLifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> activityLifecycles);


    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}
