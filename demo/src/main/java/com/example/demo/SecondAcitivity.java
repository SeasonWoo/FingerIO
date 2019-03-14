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
import android.view.Window;
import android.widget.Toast;

import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;

import java.util.ArrayList;
import java.util.List;

public class SecondAcitivity extends AppCompatActivity {
    private static final String TAG = "SecondAcitivity";
    private boolean isStart = false;
    private IPhaseBiz mPhaseBiz;
    private static final int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second_acitivity);

        //动态申请权限
        checkPermissionAndRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isStart){
            isStart = true;
            //1.得到PhaseBiz对象的实例，需要写文件的权限
            mPhaseBiz = PhaseBizProvider.providePhaseBiz(SecondAcitivity.this);
            //2.调用PhaseBiz的方法启业务功能
            mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {
                @Override
                public void receiveActionType(Gesture type) {
                    //3.在回调接口得到单个手势对象，更新UI
//                            textGesture.append(type.getStoke());

                    //如果需要，验证手势类型并做出其他的操作处理
                    if (type.equals(Gesture.HENG)) {
                        finish();
                    }
                }
            });
        }else {
            showToast("已经开启，不要重复点击");
        }
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        super.finish();
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
            if (ContextCompat.checkSelfPermission(SecondAcitivity.this, permission)
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
        Toast.makeText(SecondAcitivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
