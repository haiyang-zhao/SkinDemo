package com.zhy.skin_core;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.zhy.skin_core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zhy.skin_core.utils.SkinResources.getInstance;


public class SkinAttribute {
    private static final List<String> mAttributes = new ArrayList<>();

    //需要换肤的属性
    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }


    //需要换肤的View及其属性
    private final List<SkinView> mSkinViews = new ArrayList<>();


    //
    public void lookup(View view, AttributeSet attrs) {
        List<SkinPair> skinPairs = new ArrayList<>();
        int attrsCount = attrs.getAttributeCount();
        for (int i = 0; i < attrsCount; i++) {
            String name = attrs.getAttributeName(i);
            if (mAttributes.contains(name)) {
                String value = attrs.getAttributeValue(i);
                //固定的值如#FF00FF 色值
                if (value.startsWith("#")) {
                    continue;
                }

                int resId;
                //系统属性 android:layout_height="?actionBarSize"
                if (value.startsWith("?")) {
                    int attrId = Integer.parseInt(value.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else if (value.startsWith("@")) {
                    //正常情况 android:background="@drawable/toolbar"
                    resId = Integer.parseInt(value.substring(1));
                } else {
                    //其他
                    continue;
                }
                skinPairs.add(new SkinPair(view.getClass().getSimpleName(), name, resId));
            }
        }
        if (!skinPairs.isEmpty() || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, skinPairs);
            skinView.applySkin();
            mSkinViews.add(skinView);


        }
    }

    public void applySkin() {
        for (SkinView skinView : mSkinViews) {
            skinView.applySkin();
        }
    }

    static class SkinView {
        View view;
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        //切换皮肤
        public void applySkin() {
            applySkinSupport();

            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                String attrName = skinPair.attrName;
                int resId = skinPair.resId;
                //根据不同的属性设置不同的值
                switch (attrName) {
                    case "src":
                        Object background = getInstance().getBackground(resId);
                        //比如@color/xxx
                        if (background instanceof Integer) {
                            view.setBackgroundColor((int) background);
                        }
                        if (background instanceof Drawable) {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "background":
                        background = getInstance().getBackground(skinPair
                                .resId);
                        if (background instanceof Integer) {
                            if (view instanceof ImageView) {
                                ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                        background));
                            }else {
                                view.setBackgroundColor((Integer) background);
                            }
                        }

                        if (background instanceof Drawable) {
                            if (view instanceof ImageView) {
                                ((ImageView) view).setImageDrawable((Drawable) background);
                            } else {

                                view.setBackground((Drawable) background);
                            }
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(getInstance().getColorStateList
                                (skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        break;
                }

                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }

        //自定义view支持
        private void applySkinSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }
    }


    static class SkinPair {
        String viewName;
        String attrName;
        int resId;

        public SkinPair(String viewName, String attrName, int resId) {
            this.viewName = viewName;
            this.attrName = attrName;
            this.resId = resId;
        }
    }
}
