package com.example.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.example.demo.R;
import com.example.demo.adapters.MainPagerAdapter;
import com.example.demo.screens.HorizontalPagerFragment;
import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;


//该类实现 手势控制选择demo
public class SelectItemActivity extends AppCompatActivity {
    private static final String TAG = "SelectItemActivity";
    public static MainPagerAdapter adapter;
    public static HorizontalPagerFragment fragment;
    public static HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;

    private IPhaseBiz mPhaseBiz;
    private static final int PERMISSION_REQUEST_CODE = 0;
    private boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_item);

        //动态申请权限
        checkPermissionAndRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});

        final ViewPager viewPager = findViewById(R.id.vp_main);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        fragment = (HorizontalPagerFragment)adapter.getFragment(0);
//        horizontalInfiniteCycleViewPager =
//                fragment.getHorizontalInfiniteCycleViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragment = (HorizontalPagerFragment)adapter.getFragment(0);
        if (!isStart){
            isStart = true;
            //1.得到PhaseBiz对象的实例，需要写文件的权限
            mPhaseBiz = PhaseBizProvider.providePhaseBiz(SelectItemActivity.this);
            //2.调用PhaseBiz的方法启业务功能
            mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {

                @Override
                public void receiveActionType(Gesture type) {
                    fragment = (HorizontalPagerFragment)adapter.getFragment(0);
                    horizontalInfiniteCycleViewPager =
                            fragment.getHorizontalInfiniteCycleViewPager();
                    //3.在回调接口得到单个手势对象，更新UI
//                            textGesture.append(type.getStoke());

                    //如果需要，验证手势类型并做出其他的操作处理
                    int item = horizontalInfiniteCycleViewPager.getRealItem();
                    Log.d(TAG, "receiveActionType: item: "+item);
                    if (type.equals(Gesture.ZUO_XIE)) {
                        //在这里实现具体交互逻辑
//                        if(item == 0) horizontalInfiniteCycleViewPager.setCurrentItem(1, true);
                        if(item + 1 < 3) horizontalInfiniteCycleViewPager.setCurrentItem(item+1, true);
                    }else if(type.equals(Gesture.HENG)){
                        if(item == 1) horizontalInfiniteCycleViewPager.setCurrentItem(0, true);
                        if(item - 1 >= 0) horizontalInfiniteCycleViewPager.setCurrentItem(item-1, true);
                    }else if(type.equals(Gesture.SHU)){
                        if(item == 0 || item == 2) {
                            Intent intent = new Intent(SelectItemActivity.this, TRaxGame.class);
                            startActivity(intent);
                            //下一活动滑动方向
                            overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                        }else if(item == 1){
                            Intent intent = new Intent(SelectItemActivity.this, GesturephaseDemo.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                        }
                    }
//                    else if(type.equals(Gesture.ZUO_HU)){
//                        finish();
//                    }
                }
            });
        }else {
            showToast("已经开启，不要重复点击");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isStart) {
            mPhaseBiz.stopRecognition();
            isStart = false;
        } else {
            showToast("已经关闭,不要重复点击");
        }
    }


    private void checkPermissionAndRequest(String[] permissions) {
        List<String> permissionToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(SelectItemActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionToRequest.add(permission);
            }
        }
        if (!permissionToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionToRequest.toArray(new String[permissionToRequest.size()]),
                    PERMISSION_REQUEST_CODE);
        }
    }
    private void showToast(String msg) {
        Toast.makeText(SelectItemActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
