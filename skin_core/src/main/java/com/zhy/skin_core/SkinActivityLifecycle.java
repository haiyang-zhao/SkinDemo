package com.zhy.skin_core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhy.skin_core.utils.SkinThemeUtils;

import java.lang.reflect.Field;

//注册Activity生命周期的回调
public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "SkinActivityLifecycle";
    private SkinManager skinManager;

    public SkinActivityLifecycle(SkinManager skinManager) {

        this.skinManager = skinManager;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, activity.getClass().getName() + " onActivityCreated");


        SkinThemeUtils.updateStatusBarColor(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
//        layoutInflater = layoutInflater.cloneInContext(activity);


        //利用反射将layoutInflater的私有变量mFactorySet设置为true
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SkinLayoutInflaterFactory factory = new SkinLayoutInflaterFactory(activity);
        layoutInflater.setFactory2(factory);

        this.skinManager.addObserver(factory);

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activity.unregisterActivityLifecycleCallbacks(this);
    }
}
