package com.example.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CutoverDemo extends AppCompatActivity {
    private static final String TAG = "CutoverDemo";
    private boolean isStart = false;
    private IPhaseBiz mPhaseBiz;
    private static final int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cutover_demo);
        Log.d(TAG, "onCreate: ##########");
        //动态申请权限
        checkPermissionAndRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});

        //mPhaseBiz = PhaseBizProvider.providePhaseBiz(gesturephasedemo.this,44100,
        //        AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,44100,24000);


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isStart){
            isStart = true;
            //1.得到PhaseBiz对象的实例，需要写文件的权限
            mPhaseBiz = PhaseBizProvider.providePhaseBiz(CutoverDemo.this);
            //2.调用PhaseBiz的方法启业务功能
            mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {
                @Override
                public void receiveActionType(Gesture type) {
                    //3.在回调接口得到单个手势对象，更新UI
//                            textGesture.append(type.getStoke());

                    //如果需要，验证手势类型并做出其他的操作处理
                    if (type.equals(Gesture.ZUO_XIE)) {
                        //在这里实现具体交互逻辑
                        Intent intent = new Intent(CutoverDemo.this, SelectItemActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
    private void checkPermissionAndRequest(String[] permissions) {
        List<String> permissionToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(CutoverDemo.this, permission)
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
        Toast.makeText(CutoverDemo.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
