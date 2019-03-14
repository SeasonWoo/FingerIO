package com.example.lenovo.t_rex_game;

import android.animation.BidirectionalTypeConverter;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.LinkedList;



public class Obstacles {
    private static final String TAG = "Obstacles";
    public LinkedList<Obstacle> obstacles;
    private float minGapCactus = 120, //仙人掌最小间隔
            minGapPter = 150, //小鸟最小间隔
            gapCoefficient = 1.5f, //间隔系数
            maxDuplication = 2, //最大复制
            minSpeedCactus = 0, //仙人掌最小速度
            minSpeedPter = 8.5f; //小鸟最小速度
    private int gap = 0;
    private int xSmall, ySmall, widthSmall, heightSmall, xLarge,  yLarge,  widthLarge,  heightLarge, xPtero, yPtero, widthPtero, heightPtero;
    private GameImage mCactus_small, mCactus_big, mBird;
    Obstacles(int xSmall, int ySmall, int widthSmall, int heightSmall,
              int xLarge, int yLarge, int widthLarge, int heightLarge,
              int xPtero, int yPtero, int widthPtero, int heightPtero) {
        obstacles = new LinkedList<>();
        //小树显示的长宽高
        this.xSmall = xSmall;
        this.ySmall = ySmall;
        this.widthSmall = widthSmall;
        this.heightSmall = heightSmall;
        this.xLarge = xLarge;
        this.yLarge = yLarge;
        this.widthLarge = widthLarge;
        this.heightLarge = heightLarge;
        this.xPtero = xPtero;
        this.yPtero = yPtero;
        this.widthPtero = widthPtero;
        this.heightPtero = heightPtero;
    }
    public Obstacles(GameImage Cactus_small,GameImage Cactus_big,GameImage Bird){
        obstacles = new LinkedList<>();
        mCactus_small = Cactus_small;
        mCactus_big = Cactus_big;
        mBird = Bird;
        GameObject Cactus_small_obj = new GameObject(mCactus_small, 0, 0, 0, 0);
        widthSmall = (int)(Cactus_small_obj.bitmap.getWidth()/6);
        heightSmall = Cactus_small_obj.bitmap.getHeight();

        GameObject Cactus_big_obj = new GameObject(mCactus_big, 0, 0, 0, 0);
        widthLarge = (int)(Cactus_big_obj.bitmap.getWidth()/6);
        heightLarge = Cactus_big_obj.bitmap.getHeight();

        GameObject Bird_obj = new GameObject(mBird, 0,0,0,0);
        widthPtero = Bird_obj.bitmap.getWidth();
        heightPtero = Bird_obj.bitmap.getHeight();

    }
    public void update(float delta, float currentSpeed) {
        if(obstacles.size() > 0){
            for (Obstacle ob : obstacles) {
                ob.update(delta, currentSpeed);
            }
            if(obstacles.getFirst().xPos < - Game.WIDTH * 4) {
                obstacles.removeFirst();
            }
        }
    }

    public void createObstacles(float currentSpeed) {
        if(obstacles.size() > 0) {
            Obstacle last = obstacles.getLast();
            float minSpeed;
            if(last.xPos + last.width + gap < Game.WIDTH) {
                int duplicationCount = 0;
                int type;
                do {
                    type = 1 + (int) (Math.random() * 3);
                    for (Obstacle i: obstacles) {
                        duplicationCount = (i.type == type) ? duplicationCount + 1 : 0;
                    }
                    minSpeed = (type == 3) ? minSpeedPter : minSpeedCactus;
                } while (duplicationCount > maxDuplication );
                createObstacle(currentSpeed, type);
                getGap(currentSpeed, type, last.width);
            }
        } else {
            int type = 1 + (int) (Math.random() * 3);
            createObstacle(currentSpeed, type);
            if(obstacles.size() != 0) {
                getGap(currentSpeed, type, obstacles.getLast().width);
            }
        }
    }

    private void createObstacle (float currentSpeed, int type) {
        switch (type) {
            case 1: {
                GameObject object = new GameObject(GameImage.cactus_small, 0, 0, 0, 0);

                int size = 1 + (int) (Math.random()*3);
//                if (size > 1 && 4 > currentSpeed)
//                    size = 1;
                obstacles.addLast(new Obstacle(mCactus_small, 0, 0, widthSmall*size, heightSmall,
                        Game.WIDTH+(int)(0.01*Game.WIDTH)*size, (int)(0.80*Game.HEIGHT) , (int)(0.05*Game.WIDTH)*size ,(int)(0.15*Game.HEIGHT), type, size, 0));
                break;
            }
            case 2: {
                int size = 1 + (int) (Math.random()*3);
                if (size > 1 && 7 > currentSpeed)
                    size = 1;
                obstacles.addLast(new Obstacle(mCactus_big, 0, 0, widthLarge*size, heightLarge,
                        Game.WIDTH, (int)(0.75*Game.HEIGHT) , (int)(0.07*Game.WIDTH)*size ,(int)(0.20*Game.HEIGHT), type, size, 0));
                break;
            }
            case 3: {
                if (currentSpeed > minSpeedPter) {
//                    int height =1+(int) (Math.random() * 2);
                    int height = 2;
                    Log.d(TAG, "createObstacle: height: "+height);
                    float speedOffset = Math.random() > 0.5f ? 0.8f : -0.8f; //только для птеродактелей
                    obstacles.addLast(new Obstacle(mBird, 0, 0, widthPtero, heightPtero,
                            Game.WIDTH, (int)(0.90*Game.HEIGHT)-(int)(0.18*Game.HEIGHT)-(int)(0.09*Game.HEIGHT*(height-1)), (int)(0.09*Game.WIDTH), (int)(0.18*Game.HEIGHT), type, 1, speedOffset));
                    break;
                }
            }
        }
    }

    private void getGap(float currentSpeed, int type, int width) {
        int minGap;
        if (type == 1 || type == 2)
            minGap = Math.round(width * currentSpeed + this.minGapCactus * 0.6f);
        else
            minGap = Math.round(width * currentSpeed + this.minGapPter * 0.6f);
        int maxGap = Math.round(minGap * gapCoefficient);
        gap = minGap + (int)(Math.random()* (maxGap - minGap + 1));
    }

    public class Obstacle extends GameObject{
        //type 1.小树 2.大树 3.小鸟
        //size 每次障碍物的个数（同时出现几棵树）
        int type, size;
        private float speedOffset;
        private float j = 0;
        private boolean upWing = true;
        private Bitmap imageUp, imageDown;
        public int getType(){
            return type;
        }
        private Obstacle(GameImage img,int sourceX, int sourceY, int sourceWidth, int sourceHeight, float xPos, float yPos, int width, int height, int type, int size, float speedOffset){
            super(img,sourceX, sourceY, sourceWidth, sourceHeight, xPos, yPos, width, height);
            this.type = type;
            this.size = size;
            this.speedOffset = speedOffset;
            if(type == 3){
                GameObject temp = new GameObject(GameImage.bird_down, xPos, yPos, width, height);
                imageUp = this.bitmap;
                imageDown = temp.bitmap;
            }
            switch (type){
                case 1: {
                    addRects((int)(0.4*width), 0, (int)(0.2*width), (int)(0.40*height));
                    addRects(0, (int)(0.40*height), width, (int)(0.2*height)+(int)(0.60*height));
                    break;
                }
                case 2: {
                    addRects((int)(0.4*width), 0, (int)(0.2*width), (int)(0.4*height));
                    addRects(0, (int)(0.40*height), width, (int)(0.60*height));
                    break;
                }
                case 3: {
                    addRects((int)(0.4*width), 0, (int)(0.15*width), (int)(0.2*height));
                    addRects(0, (int)(0.2*height), (int)(0.55*width), (int)(0.3*height));
                    addRects((int)(0.4*width), (int)(0.5*height), (int)(0.6*width), (int)(0.3*height));
                    addRects((int)(0.4*width), (int)(0.8*height), (int)(0.15*width), (int)(0.2*height));
                    break;
                }
            }
        }

        void update(float delta, float currentSpeed) {
            switch (type) {
                case 1:
                case 2: {
                    xPos -= Math.floor(delta * currentSpeed * Game.FPS);
                    break;
                }
                case 3: {
                    xPos -= Math.floor(delta * Game.FPS * (currentSpeed + speedOffset));
                    j += delta;
                    if (j >= delta * 10) {
                        if (upWing)
                            this.bitmap = imageUp;
                        else
                            this.bitmap = imageDown;
                        upWing = !upWing;
                        j = 0;
                    }
                    break;
                }
            }
        }
    }
}
