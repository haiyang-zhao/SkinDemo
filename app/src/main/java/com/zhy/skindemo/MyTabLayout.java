package com.zhy.skindemo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.zhy.skin_core.SkinViewSupport;
import com.zhy.skin_core.utils.SkinResources;

/**
 * 自定义view自己实现换肤策略
 */
public class MyTabLayout extends TabLayout implements SkinViewSupport {

    int tabIndicatorColorResId;
    int tabTextColorResId;

    public MyTabLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MyTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, 0);
        tabIndicatorColorResId = a.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0);
        tabTextColorResId = a.getResourceId(R.styleable.TabLayout_tabTextColor, 0);
        a.recycle();
    }

    @Override
    public void applySkin() {
        if (tabIndicatorColorResId != 0) {
            int tabIndicatorColor = SkinResources.getInstance().getColor(tabIndicatorColorResId);
            setSelectedTabIndicatorColor(tabIndicatorColor);
        }

        if (tabTextColorResId != 0) {
            ColorStateList tabTextColor = SkinResources.getInstance().getColorStateList
                    (tabTextColorResId);
            setTabTextColors(tabTextColor);
        }
    }
}
