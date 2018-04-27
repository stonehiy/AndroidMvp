package com.ebusbar.ecore.base;

import android.app.Application;
import android.content.Context;

import com.ebusbar.ecore.delegate.AppDelegate;
import com.ebusbar.ecore.delegate.AppLifecycles;

/**
 * Application类 初始化各种配置
 */
public class BaseApplication extends Application {

    private static Context mContext;//全局上下文对象


    public static Context getContext() {
        return mContext;
    }


    private AppLifecycles mAppDelegate;

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mAppDelegate = new AppDelegate(this);
        if (null != this.mAppDelegate) {
            this.mAppDelegate.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        if (null != this.mAppDelegate) {
            this.mAppDelegate.onCreate(this);
        }
    }


    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }

    }

}
