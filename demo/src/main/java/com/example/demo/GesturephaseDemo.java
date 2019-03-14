package com.example.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;

import java.util.ArrayList;
import java.util.List;

public class GesturephaseDemo extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;

    private static final int SEEKBAR_ZERO_POINT = 30;

    private TextView textGesture;
    private IPhaseBiz mPhaseBiz;

    private boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturephasedemo);

        //动态申请权限
        checkPermissionAndRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});

        textGesture = findViewById(R.id.tv_gesture);
        Button btStart = findViewById(R.id.bt_start);
        Button btStop = findViewById(R.id.bt_stop);
        SeekBar seekBarSensitivity = findViewById(R.id.seek_bar_sensitivity);

        //mPhaseBiz = PhaseBizProvider.providePhaseBiz(gesturephasedemo.this,44100,
        //        AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,44100,24000);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart){
                    isStart = true;
                    //1.得到PhaseBiz对象的实例，需要写文件的权限
                    mPhaseBiz = PhaseBizProvider.providePhaseBiz(GesturephaseDemo.this);
                    //2.调用PhaseBiz的方法启业务功能
                    mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {
                        @Override
                        public void receiveActionType(Gesture type) {
                            //3.在回调接口得到单个手势对象，更新UI
                            textGesture.append(type.getStoke());
                            //如果需要，验证手势类型并做出其他的操作处理
                            if (type.equals(Gesture.HENG)) {
                                //在这里实现具体交互逻辑
                            }
                        }
                    });
                }else {
                    showToast("已经开启，不要重复点击");
                }
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //4.手动关闭功能释放资源
                if (isStart) {
                    mPhaseBiz.stopRecognition();
                    isStart = false;
                } else {
                    showToast("已经关闭,不要重复点击");
                }
            }
        });

        seekBarSensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPhaseBiz != null) {
                    mPhaseBiz.modifySensitivity(seekBar.getProgress() + SEEKBAR_ZERO_POINT);
                    showToast("灵敏度已更新，请关闭识别功能再打开，如果仍有问题，请检查媒体音量大小或提交BUG");
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        //4.手动关闭功能释放资源
        if (isStart) {
            mPhaseBiz.stopRecognition();
            isStart = false;
            showToast("已经关闭");
        } else {
            showToast("已经关闭,不要重复点击");
        }
    }

    private void checkPermissionAndRequest(String[] permissions) {
        List<String> permissionToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(GesturephaseDemo.this, permission)
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
        Toast.makeText(GesturephaseDemo.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
