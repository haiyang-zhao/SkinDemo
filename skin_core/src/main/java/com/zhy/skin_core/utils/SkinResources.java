package com.zhy.skin_core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public final class SkinResources {

    private static volatile SkinResources INSTANCE;

    //皮肤包名称
    private String mSkinPkgName;

    //App原有resources
    private Resources mAppResources;

    //皮肤包resources
    private Resources mSkinResources;

    //是否为默认皮肤
    private boolean isDefaultSkin = true;


    private SkinResources(Context context) {
        mAppResources = context.getResources();
    }


    public static SkinResources init(Context context) {
        if (null == INSTANCE) {
            synchronized (SkinResources.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SkinResources(context);
                }
            }
        }
        return INSTANCE;
    }

    public static SkinResources getInstance() {
        return INSTANCE;
    }



    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    public void applySkin(Resources res, String pkgName) {
        this.mSkinResources = res;
        this.mSkinPkgName = pkgName;

        isDefaultSkin = TextUtils.isEmpty(pkgName) || res == null;
    }


    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }

        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);

        return mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinID = getIdentifier(resId);
        if (skinID == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinID);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if ("color".equals(resourceTypeName)) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }
}
