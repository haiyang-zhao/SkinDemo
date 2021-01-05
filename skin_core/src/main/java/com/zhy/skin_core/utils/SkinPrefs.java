package com.zhy.skin_core.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 记录当前皮肤路径
 */
public class SkinPrefs {

    private static final String SKIN_SHARED = "skins";
    private static final String KEY_SKIN_PATH = "skin-path";
    private volatile static SkinPrefs instance;
    private final SharedPreferences mPref;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinPrefs.class) {
                if (instance == null) {
                    instance = new SkinPrefs(context.getApplicationContext());
                }
            }
        }
    }

    public static SkinPrefs getInstance() {
        return instance;
    }

    private SkinPrefs(Context context) {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    public void setSkin(String skinPath) {
        mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    public void reset() {
        mPref.edit().remove(KEY_SKIN_PATH).apply();
    }

    public String getSkin() {
        return mPref.getString(KEY_SKIN_PATH, null);
    }
}
