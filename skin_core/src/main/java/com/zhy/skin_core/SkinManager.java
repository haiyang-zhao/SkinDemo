package com.zhy.skin_core;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.zhy.skin_core.utils.SkinPrefs;
import com.zhy.skin_core.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

public final class SkinManager extends Observable {

    private volatile static SkinManager INSTANCE;

    private final Application app;
    private final SkinActivityLifecycle skinActivityLifecycle;

    private SkinManager(Application app) {
        this.app = app;
        this.skinActivityLifecycle = new SkinActivityLifecycle(this);
        SkinPrefs.init(this.app);
        SkinResources.init(this.app);
        app.registerActivityLifecycleCallbacks(skinActivityLifecycle);


    }

    public static void init(Application application) {
        if (null == INSTANCE) {
            synchronized (SkinManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SkinManager(application);
                }
            }
        }

    }


    public static SkinManager getInstance() {
        return INSTANCE;
    }

    /**
     * 加载皮肤包
     *
     * @param path 皮肤路径 如果为空则使用默认皮肤
     */
    public void loadSkin(String path) {
        if (TextUtils.isEmpty(path)) {
            SkinPrefs.getInstance().reset();
            SkinResources.getInstance().reset();

        } else {

            //宿主res
            Resources appResources = app.getResources();
            try {
                //反射创建assets对象
                AssetManager assetManager = AssetManager.class.newInstance();
                //反射获取addAssetPath方法
                Method method = AssetManager.class.getMethod("addAssetPath", String.class);
                method.invoke(assetManager, path);


                //根据当前的设备显示器信息 与 配置(横竖屏、语言等) 创建Resources
                Resources skinResource = new Resources(assetManager, appResources.getDisplayMetrics
                        (), appResources.getConfiguration());

                //获取外部Apk(皮肤包) 包名
                PackageManager mPm = app.getPackageManager();
                PackageInfo info = mPm.getPackageArchiveInfo(path, PackageManager
                        .GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResources.getInstance().applySkin(skinResource, packageName);

                //记录
                SkinPrefs.getInstance().setSkin(path);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //通知采集的View 更新皮肤
        //被观察者改变 通知所有观察者
        setChanged();
        notifyObservers(null);

    }

}
