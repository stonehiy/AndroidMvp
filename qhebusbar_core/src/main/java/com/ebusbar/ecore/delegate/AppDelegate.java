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
package com.ebusbar.ecore.delegate;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑,因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候,就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication,只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * <p>
 * ================================================
 */
public class AppDelegate implements AppLifecycles {

    private List<IModuleConfig> mIModuleConfigs;
    private List<AppLifecycles> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    public AppDelegate(Context context) {
    }

    @Override
    public void attachBaseContext(Context base) {

        //初始化Manifest文件解析器，用于解析组件在自己的Manifest文件配置的Application
        ManifestParser manifestParser = new ManifestParser(base);
        mIModuleConfigs = manifestParser.parse();
        //解析得到的组件Application列表之后，给每个组件Application注入
        //context，和Application的生命周期的回调，用于实现application的同步
        if (mIModuleConfigs != null && mIModuleConfigs.size() > 0) {
            for (IModuleConfig configModule :
                    mIModuleConfigs) {
                configModule.injectAppLifecycle(base, mAppLifecycles);
                configModule.injectActivityLifecycle(base, mActivityLifecycles);
            }
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles life :
                    mAppLifecycles) {
                life.attachBaseContext(base);
            }
        }

    }

    @Override
    public void onCreate(Application application) {
        //相应调用组件Application代理类的onCreate方法
        for (IModuleConfig module : mIModuleConfigs) {
            module.applyOptions(application);
        }
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles life : mAppLifecycles) {
                life.onCreate(application);
            }
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks life :
                    mActivityLifecycles) {
                application.registerActivityLifecycleCallbacks(life);
            }
        }


    }

    @Override
    public void onTerminate(Application application) {

        //相应调用组件Application代理类的onTerminate方法
        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles life :
                    mAppLifecycles) {
                life.onTerminate(application);
            }
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks life :
                    mActivityLifecycles) {
                application.unregisterActivityLifecycleCallbacks(life);
            }
        }

    }

}

