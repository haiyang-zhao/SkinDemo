package com.zhy.skindemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.zhy.skindemo.fragment.MusicFragment;
import com.zhy.skindemo.fragment.MyFragmentPagerAdapter;
import com.zhy.skindemo.fragment.RadioFragment;
import com.zhy.skindemo.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Fragment> fragments = new ArrayList<Fragment>();
    private final List<String> titles = Arrays.asList("音乐", "电台", "视频");
    private final static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments.add(new MusicFragment());
        fragments.add(new RadioFragment());
        fragments.add(new VideoFragment());
        TabLayout tabLayout = findViewById(R.id.tabView);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);


        if (!checkIfPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }


    private boolean checkIfPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        result = result && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public void skinSelect(View view) {
        startActivity(new Intent(this, SkinActivity.class));
    }
}