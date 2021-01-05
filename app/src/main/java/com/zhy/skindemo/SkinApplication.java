package com.zhy.skindemo;

import com.zhy.skin_core.SkinBaseApp;
import com.zhy.skin_core.SkinManager;

public class SkinApplication extends SkinBaseApp {
    @Override
    public void onCreate() {
        super.onCreate();

        SkinManager.init(this);
    }
}
