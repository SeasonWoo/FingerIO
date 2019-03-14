package com.example.lenovo.t_rex_game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class TRex extends GameObject{
    private static final String TAG = "TRex";
    public GameObject TRexLeft, TRexRight,
            TRexSpace, TRexBlink, TRexCrashed,
            TRexDuck1, TRexDuck2;
    private float i = 0;
    private boolean left = true;
    private static float Initial_Velocity = -25;
    private float gravity = 0.5f, dropVelocity = -5, velocity = Initial_Velocity;
    private int distance = (int)(0.5*Game.HEIGHT);
    //恐龙俯身行走距离
    private float downDistance = 0;

    private float blinkDelay = (float) Math.ceil(Math.random() * 7000);
    private boolean blink = false;

    public boolean halfJump = false, spacePressed = false, downPressed = false;
    private boolean jumping = false;
    float landWaitX = 45;
    public int speedDropCoeff = 1;

    public TRex(GameImage Trax, GameImage Trax_duck) {
        super(Trax, 0, (int)(0.75*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.2*Game.HEIGHT));
        int width = (int)(this.bitmap.getWidth()/6);
        int height = bitmap.getHeight();
        TRexSpace = new GameObject(Trax, 0, 0, width, height,
                0, (int)(0.75*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.2*Game.HEIGHT));;
        TRexBlink = new GameObject(Trax, width, 0, width, height,
                0, (int)(0.75*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.2*Game.HEIGHT));
        TRexCrashed = new GameObject(Trax, width*5, 0, width, height,
                0, (int)(0.75*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.2*Game.HEIGHT));
        TRexRight = new GameObject(Trax, width*2, 0, width, height,
                0, (int)(0.75*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.2*Game.HEIGHT));
        TRexLeft = new GameObject(Trax, width*3, 0, width, height,
                0, (int)(0.75*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.2*Game.HEIGHT));
        GameObject TRexDuck = new GameObject(Trax_duck, 0, (int)(0.84*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.11*Game.HEIGHT));
        int Trac_duck_width = (int)(TRexDuck.bitmap.getWidth()/2);
        int Trac_duck_height = (int)(TRexDuck.bitmap.getHeight());
        TRexDuck1 = new GameObject(Trax_duck, 0, 0, Trac_duck_width, Trac_duck_height,
                0, (int)(0.84*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.11*Game.HEIGHT));
        TRexDuck2 = new GameObject(Trax_duck, Trac_duck_width, 0, Trac_duck_width, Trac_duck_height,
                0, (int)(0.84*Game.HEIGHT), (int)(0.1*Game.WIDTH), (int)(0.11*Game.HEIGHT));

        //根据恐龙的外形，将恐龙划分为四个矩形
        addRects((int)0.1*Trac_duck_width, 0, (int)0.8*Trac_duck_width, Trac_duck_height);
        addRects((int)(0.6*width), 0, (int)(0.4*width), (int)(0.25*height));
        addRects(1, (int)(0.25*height), (int)(0.7*width), (int)(0.35*height));
        addRects((int)(0.2*width), (int)(0.6*height), (int)(0.5*width), (int)(0.2*height));
        addRects((int)(0.3*width), (int)(0.8*height), (int)(0.3*width), (int)(0.2*height));
    }

    public boolean updateJump(float delta, float currentSpeed) {
        if(!jumping) {
            //和当前速度成正相关，跑步速度越快，跳跃速度越快
            velocity = velocity - currentSpeed / 10;
            jumping = true;
        }
        TRexSpace.yPos += Math.round(velocity);
        velocity += gravity * speedDropCoeff;

        //高度超过distance，此时速度是负数，小于则是绝对值大于
        if(TRexSpace.yPos <= 0.75*Game.HEIGHT - distance && velocity < dropVelocity) {
            velocity = dropVelocity;
        }
        //高度低于distance，但又高于0.9distance
        if(halfJump && TRexSpace.yPos >= 0.75*Game.HEIGHT - 0.85*distance && TRexSpace.yPos <= 0.75*Game.HEIGHT - 0.8*distance && velocity < dropVelocity) {
            velocity = dropVelocity;
            halfJump = false;
        }
        if(TRexSpace.yPos > 0.75*Game.HEIGHT) {
            TRexSpace.yPos = (float) 0.75*Game.HEIGHT;
            velocity = Initial_Velocity; //初速度
            spacePressed = false;
            halfJump = false;
            jumping = false;
            return false;
        }
        return true;
    }

    public void updateBlink(float delta) {
        i += delta;
        if (i > blinkDelay / 1000 && !blink) {
            blink = true;
        } else if (i > (blinkDelay / 1000) + delta * 10) {
            i = 0;
            blink = false;
            blinkDelay = (float) Math.ceil(Math.random() * 7000);
        }
    }

    public void updateWalk(float delta) {
        i += delta;
        if (i >= delta * 5) {
            left = !left;
            i = 0;
        }
        if(downPressed){
            downDistance += delta;
        }
        if(downDistance >= delta * 80){
            downDistance = 0;
            downPressed = false;
        }
    }

    public boolean updateBegin(float delta) {
        if (TRexLeft.xPos <= 0.03*Game.WIDTH || TRexRight.xPos <= 0.03*Game.WIDTH) {
            TRexLeft.xPos = TRexRight.xPos = TRexSpace.xPos = TRexCrashed.xPos = TRexDuck1.xPos
                    = TRexDuck2.xPos += delta * Game.FPS;
            landWaitX += (float) (Game.WIDTH - 45) / (float) (0.03*Game.WIDTH / (delta * Game.FPS)); //(delta * FPS) = 1
            return false;
        } else {
            TRexLeft.xPos = TRexRight.xPos = TRexSpace.xPos = TRexCrashed.xPos = TRexDuck1.xPos
                    = TRexDuck2.xPos = (float) (0.03*Game.WIDTH);
            return true;
        }
    }

    public boolean checkHeight () {
        if(TRexSpace.yPos >= 0.5*Game.HEIGHT-TRexSpace.height) {
            return true;
        } else
            return false;
    }

    public void restart(){
        TRexSpace.xPos = xPos = (float) 0.03*Game.WIDTH;
        TRexSpace.yPos = yPos = (float) 0.75*Game.HEIGHT;
        spacePressed = false;
        downPressed = false;
        halfJump = false;
    }

//  检查碰撞
public boolean checkCollision(GameObject ob) {
    /**开挂**/
    if(ob.xPos - this.TRexSpace.xPos <= (float)0.25*Game.WIDTH
                && ob.xPos - this.TRexSpace.xPos >= 0){
        Obstacles.Obstacle obstacle = (Obstacles.Obstacle) ob;
        if(((Obstacles.Obstacle) ob).type != 3){
            spacePressed = true;
            speedDropCoeff = 1;
        }else {
            downPressed = true;
        }
    }
    /**开挂**/

        this.TRexSpace.setOriginalRect();
        this.TRexDuck1.setOriginalRect();
        ob.setOriginalRect();
        if(Rect.intersects(TRexSpace.originalRect, ob.originalRect) ) {
            int first = downPressed ? 0 : 1;
            int last = downPressed ? 1 : rects.size();
//            int first = 0;
//            int last = rects.size();
            Log.d(TAG, "checkCollision: in checkCollision");
            for (int i = first; i < last; i++) {
                for (int j = 0; j < ob.rects.size(); j++) {
                    Rect boxTRex;
                    if(!downPressed) boxTRex = creatAdjustedRect(TRexSpace.originalRect, rects.get(i));
                    else boxTRex = creatAdjustedRect(TRexDuck1.originalRect, rects.get(i));
                    Rect boxObstacle = creatAdjustedRect(ob.originalRect, ob.rects.get(j));
                    Log.d(TAG, "checkCollision:** "+i+" "+j);
                    if (Rect.intersects(boxTRex, boxObstacle) ) {
                        Rect rect = new Rect(1, 3, 10, 15);
                        int offset = downPressed ? 1 : 0;
                        TRexCrashed.xPos += offset;
                        TRexCrashed.yPos = TRexSpace.yPos;

                        velocity = Initial_Velocity;
                        jumping = false;
                        return true;
                    }
                }
            }
//            int offset = downPressed ? 1 : 0;
//            TRexCrashed.xPos += offset;
//            TRexCrashed.yPos = yPos;
//
//            velocity = -20; //начальная скорость
//            jumping = false;
//            return true;
        }
        return false;
    }

    public void paint(Canvas canvas, boolean waiting, boolean running, boolean gameover){
        if(gameover)
            TRexCrashed.paint(canvas);
        else if(!waiting) {
            if (!running) {
                if (left && !spacePressed)
                    TRexLeft.paint(canvas);
                else if (!left && !spacePressed)
                    TRexRight.paint(canvas);
                else if (spacePressed)
                    TRexSpace.paint(canvas);
            }
            if (running && !spacePressed) {
                if(!downPressed) {
                    if (left)
                        TRexLeft.paint(canvas);
                    else
                        TRexRight.paint(canvas);
                } else {
                    if (left)
                        TRexDuck1.paint(canvas);
                    else
                        TRexDuck2.paint(canvas);
                }
            }
            if (running && spacePressed) {
                TRexSpace.paint(canvas);
            }
        } else {
            if(!blink)
                TRexSpace.paint(canvas);
            else
                TRexBlink.paint(canvas);
        }
    }
}
