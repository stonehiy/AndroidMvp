package com.ebusbar.ecore.delegate;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ManifestParser 会对其中 value 为 IModuleConfig 的 meta-data 进行解析，并通过反射生成实例。
 */
public final class ManifestParser {
    private static final String MODULE_VALUE = "IModuleConfig";
    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<IModuleConfig> parse() {
        List<IModuleConfig> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    //会对其中value为IModuleConfig的meta-data进行解析，并通过反射生成实例
                    if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse IModuleConfig", e);
        }
        return modules;
    }

    //通过类名生成实例
    private static IModuleConfig parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find IModuleConfig implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate IModuleConfig implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate IModuleConfig implementation for " + clazz, e);
        }

        if (!(module instanceof IModuleConfig)) {
            throw new RuntimeException("Expected instanceof IModuleConfig, but found: " + module);
        }
        return (IModuleConfig) module;
    }
}