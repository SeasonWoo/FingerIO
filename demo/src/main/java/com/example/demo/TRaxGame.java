package com.example.demo;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.t_rex_game.*;

import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;

import java.util.ArrayList;
import java.util.List;

public class TRaxGame extends AppCompatActivity {
    private static final String TAG = "TRaxGame";
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final int SEEKBAR_ZERO_POINT = 30;
    private IPhaseBiz mPhaseBiz;
    private boolean isStart = false;

    private void checkPermissionAndRequest(String[] permissions) {
        List<String> permissionToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(TRaxGame.this, permission)
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
        Toast.makeText(TRaxGame.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    Game game;
    static float FPS = 50;
    //布尔游戏状态变量
    private boolean run = false, waiting = true, gameover = false, running = false;
    //主窗口设置
    static int WIDTH;
    static int HEIGHT;
    private static String NAME = "T-Rex Game";


    //游戏设置
    private float speed = 14, maxSpeed = 20, currentSpeed = 14, acceleration = 0.001f, distance = 0,
            coefficient = 0.025f; //距离测量系数
    private long gameOverTimer = 0;

    private Obstacles obstacles;
    private TRex tRex;
    private HorizonLine horizon;
//    private GameOverPanel gameOverPanel;
//    private DistanceMeter distanceMeter;

    public static ImageView iv_canvas;
    public static Resources resourcesInstance;

    public static final int BITMAT = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BITMAT:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (bitmap == null) {
                        Log.e(TAG, "handleMessage: bitmap is null");
                        break;
                    }
                    iv_canvas.setImageBitmap(bitmap);
                    iv_canvas.invalidate();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        //动态申请权限
        checkPermissionAndRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO});

        resourcesInstance = this.getResources();
        iv_canvas = findViewById(R.id.iv_canvas);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;
        game = new com.example.lenovo.t_rex_game.Game(WIDTH,
                HEIGHT, resourcesInstance, iv_canvas);

        Log.d(TAG, "onCreate: WIDTH " + WIDTH + " HEIGHT " + HEIGHT);
        iv_canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!game.tRex.downPressed) {
                            game.tRex.spacePressed = true;
                            game.tRex.speedDropCoeff = 1;
                        }
                        if (game.gameover)
                            game.restart();
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        if (game.tRex.checkHeight() && game.tRex.spacePressed && !game.waiting) {
                            game.tRex.halfJump = true;
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        game.init();
        game.start();
        if (!isStart) {
            isStart = true;
            //1.得到PhaseBiz对象的实例，需要写文件的权限
            mPhaseBiz = PhaseBizProvider.providePhaseBiz(TRaxGame.this);
            //2.调用PhaseBiz的方法启业务功能
            mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {
                @Override
                public void receiveActionType(Gesture type) {
                    //如果需要，验证手势类型并做出其他的操作处理
                    Log.d(TAG, "receiveActionType: "+type);
                    if (type.equals(Gesture.ZUO_XIE)) {
                        Log.d(TAG, "receiveActionType: 111");
                        //在这里实现具体交互逻辑
                        if (!game.tRex.downPressed) {
                            game.tRex.spacePressed = true;
                            game.tRex.speedDropCoeff = 1;
                        }
                        if (game.gameover)
                            game.restart();
                        if (game.tRex.checkHeight() && game.tRex.spacePressed && !game.waiting) {
                            game.tRex.halfJump = true;
                        }
                    }else if(type.equals(Gesture.HENG) || type.equals(Gesture.SHU)){
                        Log.d(TAG, "receiveActionType: 222");
                        game.tRex.downPressed = true;
                    }else if(type.equals(Gesture.ZUO_HU)){
                        Log.d(TAG, "receiveActionType: 333");
                        finish();
                    }
                }
            });
            showToast("已经开启");
        } else {
            showToast("已经开启，不要重复点击");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.stop();
        //4.手动关闭功能释放资源
        if (isStart) {
            mPhaseBiz.stopRecognition();
            isStart = false;
            showToast("已经关闭");
        } else {
            showToast("已经关闭,不要重复点击");
        }
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        super.finish();
    }

}


