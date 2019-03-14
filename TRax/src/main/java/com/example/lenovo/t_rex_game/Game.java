package com.example.lenovo.t_rex_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class Game implements Runnable{
    public Game(int width, int height, Resources resourcesInstance, ImageView iv_canvas){
        WIDTH = width;
        HEIGHT = height;
        this.resourcesInstance = resourcesInstance;
        this.iv_canvas = iv_canvas;
    }
    Game game;
    static float FPS = 40;
    //布尔游戏状态变量
    public boolean run = false, waiting = true, gameover = false, running = false;
    //主窗口设置
    static int WIDTH;
    static int HEIGHT;
    private static String NAME = "T-Rex Game";

    private static String audioPath = "";
    static String path = "";
    //游戏设置
    private float speed = 14, maxSpeed = 20, currentSpeed = 14, acceleration = 0.001f, distance = 0,
            coefficient = 0.025f; //距离测量系数
    private long gameOverTimer = 0;

    private Obstacles obstacles;
    public TRex tRex;
    private HorizonLine horizon;
//    private GameOverPanel gameOverPanel;
//    private DistanceMeter distanceMeter;

    public static ImageView iv_canvas;
    private static final String TAG = "Game";

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
//                    Log.d(TAG, "handleMessage: has set bitmap");
                    iv_canvas.invalidate();
                    break;
                default:
                    break;
            }
        }
    };
    public void start() {
        run = true;
        new Thread(this).start();
    }

    public void stop() {
        run = false;
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double ns = 1000000000; //??
        float delta = 0.0f;

        while (run) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= (float) 1 / FPS) {
                update(delta); //更新场景参数
                delta -= (float) 1 / FPS;
            }

            render(); //场景渲染
        }
    }

    public void init() {
        horizon = new HorizonLine(GameImage.land, GameImage.cloud);
        obstacles = new Obstacles(GameImage.cactus_small, GameImage.cactus_big, GameImage.bird_up);
        tRex = new TRex(GameImage.trax, GameImage.trax_duck);
    }

    private void update(float delta) {
        if (!gameover) {
            if(!waiting && !running){
                running = tRex.updateBegin(delta);
            }
            if(!waiting){
                tRex.updateWalk(delta);
                distance += currentSpeed * delta * FPS * coefficient;
            }else if (waiting && !tRex.spacePressed) {   //blinking
                tRex.updateBlink(delta);
            }
            if (tRex.spacePressed) {
                boolean temp = tRex.updateJump(delta, currentSpeed);
                if (waiting)
                    waiting = temp;
            }

            if(running) {
                if (currentSpeed < maxSpeed)
                    currentSpeed += acceleration;
                horizon.update(delta, currentSpeed);
                if(distance > 30) {
                    //create obstacles
                    obstacles.createObstacles(currentSpeed);
                    //update obstacles
                    obstacles.update(delta, currentSpeed);
                }
                for (int i = 0; i < obstacles.obstacles.size(); i++) {
                    gameover = tRex.checkCollision(obstacles.obstacles.get(i));
                    if(gameover) {
                        gameOverTimer = System.currentTimeMillis();
                        break;
                    }
                }
            }
        }
    }

    private void render() {
        Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        canvas.drawColor(Color.parseColor("#f7f7f7"));

        horizon.paint(canvas, waiting, running);
        if(!waiting) {
            if (running) {
                for (int i = 0; i < obstacles.obstacles.size(); i++) {
                    GameObject temp = obstacles.obstacles.get(i);
                    temp.paint(canvas);
                }
            }
        }
        tRex.paint(canvas, waiting, running, gameover);

        if(gameover) {
//            g.drawImage(gameOverPanel.restartButton, gameOverPanel.restartTargetX, gameOverPanel.restartTargetY, gameOverPanel.restartTargetWidth, gameOverPanel.restartTargetHeight, null);
//            g.drawImage(gameOverPanel.text, gameOverPanel.textTargetX, gameOverPanel.textTargetY, gameOverPanel.textTargetWidth, gameOverPanel.textTargetHeight, null);
        }


        //需要数据传递，用下面方法；
        Message msg = new Message();
        msg.what = BITMAT;
        msg.obj = bitmap;//可以是基本类型，可以是对象，可以是List、map等；
        mHandler.sendMessage(msg);
    }
    public void restart() {
//        if(System.currentTimeMillis() - gameOverTimer > 500) {
        running = true;
        gameover = false;
        tRex.restart();
        currentSpeed = speed;
        obstacles.obstacles.clear();
        distance = 0;
//        }
    }

}
