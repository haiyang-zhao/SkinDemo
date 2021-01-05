package com.zhy.skindemo;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhy.skin_core.SkinManager;

import java.io.File;

public class SkinActivity extends AppCompatActivity {
    private static final File SDCARD = Environment.getExternalStorageDirectory();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

    }

    public void change(View view) {
        SkinManager.getInstance().loadSkin("/storage/emulated/0/Android/media/com.zhy.skindemo/skin_1.apk");
    }

    public void restore(View view) {

        SkinManager.getInstance().loadSkin(null);
    }
}
