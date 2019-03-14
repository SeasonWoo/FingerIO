package com.example.demo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;
import com.nightonke.wowoviewpager.Animation.WoWoTextViewTextAnimation;
import com.nightonke.wowoviewpager.Enum.Typewriter;
import com.nightonke.wowoviewpager.Enum.WoWoTypewriter;
import com.nightonke.wowoviewpager.WoWoViewPager;

import java.util.ArrayList;
import java.util.List;

public class WoWoTextViewTextAnimationActivity extends WoWoActivity {
    private static final String TAG = "WoWoTextViewTextAnimati";
    private boolean isStart = false;
    private IPhaseBiz mPhaseBiz;
    private static final int PERMISSION_REQUEST_CODE = 0;
    @Override
    protected int contentViewRes() {
        return R.layout.activity_wo_wo_text_view_text_animation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态申请权限
        checkPermissionAndRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});

        //mPhaseBiz = PhaseBizProvider.providePhaseBiz(gesturephasedemo.this,44100,
        //        AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,44100,24000);

        addTextAnimation(findViewById(R.id.test0), WoWoTypewriter.InsertFromLeft);
        addTextAnimation(findViewById(R.id.test1), WoWoTypewriter.DeleteThenType);
        addTextAnimation(findViewById(R.id.test2), WoWoTypewriter.InsertFromRight);

        //wowo.setScrollDuration(1000);
        wowo.setEase(ease);
        wowo.setUseSameEaseBack(useSameEaseTypeBack);
        //wowo.setDirection(WoWoViewPager.Vertical);
        wowo.ready();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isStart){
            isStart = true;
            //1.得到PhaseBiz对象的实例，需要写文件的权限
            mPhaseBiz = PhaseBizProvider.providePhaseBiz(WoWoTextViewTextAnimationActivity.this);
            //2.调用PhaseBiz的方法启业务功能
            mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {
                @Override
                public void receiveActionType(Gesture type) {
                    //3.在回调接口得到单个手势对象，更新UI
//                            textGesture.append(type.getStoke());

                    //如果需要，验证手势类型并做出其他的操作处理
                    if (type.equals(Gesture.ZUO_XIE)) {
                        //在这里实现具体交互逻辑
                        wowo.next();
                    }else if(type.equals(Gesture.HENG)){
                        wowo.previous();
                    }
                    else if(type.equals(Gesture.SHU)){
                        Log.d(TAG, "receiveActionType: "+111);
                        Intent intent = new Intent(WoWoTextViewTextAnimationActivity.this, SelectItemActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                    }
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

    private void addTextAnimation(View view, Typewriter typewriter) {
        wowo.addAnimation(view)
                .add(WoWoTextViewTextAnimation.builder().page(0)
                        .from("向左滑动").to("FingerIO").typewriter(typewriter).build())
                .add(WoWoTextViewTextAnimation.builder().page(1)
                        .from("FingerIO").to("不一样的交互方式").typewriter(typewriter).build())
                .add(WoWoTextViewTextAnimation.builder().page(2)
                        .from("不一样的交互方式").to("定义全新操作").typewriter(typewriter).build())
                .add(WoWoTextViewTextAnimation.builder().page(3).start(0).end(0.5)
                        .from("定义全新操作").to("方便快捷").typewriter(typewriter).build())
                .add(WoWoTextViewTextAnimation.builder().page(3).start(0.5).end(1)
                        .from("方便快捷").to("向上滑动").typewriter(typewriter).build());
    }
    private void checkPermissionAndRequest(String[] permissions) {
        List<String> permissionToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(WoWoTextViewTextAnimationActivity.this, permission)
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
        Toast.makeText(WoWoTextViewTextAnimationActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
