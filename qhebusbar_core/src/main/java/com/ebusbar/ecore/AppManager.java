/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ebusbar.ecore;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;


import com.ebusbar.ecore.base.BaseApplication;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * ================================================
 * 用于管理所有 activity,和在前台的 activity
 * 可以通过直接持有 AppManager 对象执行对应方法
 * ================================================
 */
public class AppManager {

    protected final String TAG = this.getClass().getSimpleName();
    //管理所有activity
    public List<Activity> mActivityList;
    //当前在前台的activity
    private Activity mCurrentActivity;

    private static AppManager mAppManager;

    private AppManager() {
    }


    public static AppManager getInstance() {
        if (null == mAppManager) {
            mAppManager = new AppManager();
        }
        return mAppManager;

    }


    /**
     * 释放资源
     */
    public void release() {
        mActivityList.clear();
        mActivityList = null;
        mCurrentActivity = null;
    }

    /**
     * 将在前台的 activity 赋值给 currentActivity,注意此方法是在 onResume 方法执行时将栈顶的 activity 赋值给 currentActivity
     * 所以在栈顶的 activity 执行 onCreate 方法时使用 {@link #getCurrentActivity()} 获取的就不是当前栈顶的 activity,可能是上一个 activity
     * 如果在 App 的第一个 activity 执行 onCreate 方法时使用 {@link #getCurrentActivity()} 则会出现返回为 null 的情况
     * 想避免这种情况请使用 {@link #getTopActivity()}
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    /**
     * 获取在前台的 activity (保证获取到的 activity 正处于可见状态,即未调用 onStop),获取的 activity 存续时间
     * 是在 onStop 之前,所以如果当此 activity 调用 onStop 方法之后,没有其他的 activity 回到前台(用户返回桌面或者打开了其他 App 会出现此状况)
     * 这时调用 {@link #getCurrentActivity()} 有可能返回 null,所以请注意使用场景和 {@link #getTopActivity()} 不一样
     * <p>
     * Example usage:
     * 使用场景比较适合,只需要在可见状态的 activity 上执行的操作
     * 如当后台 service 执行某个任务时,需要让前台 activity ,做出某种响应操作或其他操作,如弹出 Dialog,这时在 service 中就可以使用 {@link #getCurrentActivity()}
     * 如果返回为 null ,说明没有前台 activity (用户返回桌面或者打开了其他 App 会出现此状况),则不做任何操作,不为 null ,则弹出 Dialog
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity != null ? mCurrentActivity : null;
    }

    /**
     * 获取位于栈顶的 activity,此方法不保证获取到的 acticity 正处于可见状态,即使 App 进入后台也会返回当前栈顶的 activity
     * 因此基本不会出现 null 的情况,比较适合大部分的使用场景,如 startActivity,Glide 加载图片
     *
     * @return
     */
    public Activity getTopActivity() {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when getTopActivity()");
            return null;
        }
        return mActivityList.size() > 0 ? mActivityList.get(mActivityList.size() - 1) : null;
    }


    /**
     * 返回一个存储所有未销毁的 activity 的集合
     *
     * @return
     */
    public List<Activity> getActivityList() {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        return mActivityList;
    }


    /**
     * 添加 activity 到集合
     */
    public void addActivity(Activity activity) {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        synchronized (AppManager.class) {
            if (!mActivityList.contains(activity)) {
                mActivityList.add(activity);
            }
        }
    }

    /**
     * 删除集合里的指定的 activity 实例
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when removeActivity(Activity)");
            return;
        }
        synchronized (AppManager.class) {
            if (mActivityList.contains(activity)) {
                mActivityList.remove(activity);
            }
        }
    }

    /**
     * 删除集合里的指定位置的 activity
     *
     * @param location
     */
    public Activity removeActivity(int location) {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when removeActivity(int)");
            return null;
        }
        synchronized (AppManager.class) {
            if (location > 0 && location < mActivityList.size()) {
                return mActivityList.remove(location);
            }
        }
        return null;
    }

    /**
     * 关闭指定的 activity class 的所有的实例
     *
     * @param activityClass
     */
    public void killActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when killActivity(Class)");
            return;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                activity.finish();
            }
        }
    }


    /**
     * 指定的 activity 实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean activityInstanceIsLive(Activity activity) {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when activityInstanceIsLive(Activity)");
            return false;
        }
        return mActivityList.contains(activity);
    }


    /**
     * 指定的 activity class 是否存活(同一个 activity class 可能有多个实例)
     *
     * @param activityClass
     * @return
     */
    public boolean activityClassIsLive(Class<?> activityClass) {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when activityClassIsLive(Class)");
            return false;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取指定 activity class 的实例,没有则返回 null(同一个 activity class 有多个实例,则返回最早的实例)
     *
     * @param activityClass
     * @return
     */
    public Activity findActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Log.w(TAG, "mActivityList == null when findActivity(Class)");
            return null;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                return activity;
            }
        }
        return null;
    }


    /**
     * 关闭所有 activity
     */
    public void killAll() {
//        while (getActivityList().size() != 0) { //此方法只能兼容LinkedList
//            getActivityList().remove(0).finish();
//        }

        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            iterator.remove();
            next.finish();
        }
    }

    /**
     * 关闭所有 activity,排除指定的 activity
     *
     * @param excludeActivityClasses activity class
     */
    public void killAll(Class<?>... excludeActivityClasses) {
        List<Class<?>> excludeList = Arrays.asList(excludeActivityClasses);
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();

            if (excludeList.contains(next.getClass()))
                continue;

            iterator.remove();
            next.finish();
        }
    }

    /**
     * 关闭所有 activity,排除指定的 activity
     *
     * @param excludeActivityName activity 的完整全路径
     */
    public void killAll(String... excludeActivityName) {
        List<String> excludeList = Arrays.asList(excludeActivityName);
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();

            if (excludeList.contains(next.getClass().getName()))
                continue;

            iterator.remove();
            next.finish();
        }
    }


    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            killAll();
            release();
            ActivityManager activityMgr =
                    (ActivityManager) BaseApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(BaseApplication.getContext().getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
