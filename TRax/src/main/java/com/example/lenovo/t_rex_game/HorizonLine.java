package com.example.lenovo.t_rex_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.LinkedList;

public class HorizonLine {
    public Land lands;
    public LinkedList<Cloud> clouds;
    Context mContext;

    public HorizonLine(GameImage land, GameImage cloud) {
        lands = new Land(land);
        clouds = new LinkedList<>();
        clouds.push(new Cloud(cloud, 0, 0, (int)(0.07*Game.WIDTH), (int)(0.13*Game.HEIGHT)));
    }

    public void update(float delta, float currentSpeed) {
        lands.update(delta, currentSpeed);
        //update clouds
        for(Cloud curCloud : clouds){
            curCloud.xPos -= curCloud.update(delta, currentSpeed);
        }
        Cloud lastCloud = clouds.getLast();
        float cloudGap = (float)0.1*Game.WIDTH + (int) (Math.random()*0.1*Game.WIDTH);
        if (clouds.size() < lastCloud.maxCloudsCount && (Game.WIDTH - lastCloud.xPos) > cloudGap && lastCloud.freqCloud > Math.random() * 10) {
            clouds.addLast(new Cloud(lastCloud.mImg, Game.WIDTH + lastCloud.width, 0, lastCloud.width, lastCloud.height));
        }
        Cloud firstCloud = clouds.getFirst();
        if (firstCloud.xPos < -firstCloud.width) {
            clouds.removeFirst();
        }
    }

    public int getLandHeight() {
        return lands.land1.height;
    }

    public void paint(Canvas canvas, boolean waiting, boolean running) {
        lands.paint(canvas, waiting, running);
        for (int i = 0; i < clouds.size(); i++) {
            Cloud curCloud = clouds.get(i);
            curCloud.paint(canvas);
        }
    }

    private class Cloud extends GameObject {
        //macCloud是云的最低高度，minCloud是云的最高高度，maxCloudsCount是云数组中最大云的数量
        public int maxCloud = (int)0.4*Game.HEIGHT, minCloud = (int)0.05*Game.HEIGHT, maxCloudsCount = 6;
        public float speedCloud = 0.3f, freqCloud = 0.2f;
        private GameImage mImg;

        public Cloud(GameImage img, float xPos, float yPos, int width, int height) {
            super(img, xPos, yPos, width, height);
            this.yPos = minCloud + (int) (Math.random() * maxCloud);

            mImg = img;
        }

        public float update(float delta, float currentSpeed) {

            float speedCloud = this.speedCloud / (delta * currentSpeed * 2);

            return speedCloud;
        }

    }

    private class Land extends GameObject {
        public GameObject land1, land2;

        public Land(GameImage img) {
            super(img, (int)(0.85*Game.HEIGHT), Game.WIDTH, (int)(0.15*Game.HEIGHT), Game.WIDTH);
//            land1 = this;
            land1 = new GameObject(img, 0, 0, (int)(0.6*this.bitmap.getWidth()), this.bitmap.getHeight(),
                    0, (int)(0.85*Game.HEIGHT), Game.WIDTH, (int)(0.15*Game.HEIGHT));
            land2 = new GameObject(img, (int)(0.6*this.bitmap.getWidth()), 0, (int)(0.4*this.bitmap.getWidth()), this.bitmap.getHeight(),
                    Game.WIDTH, (int)(0.85*Game.HEIGHT), Game.WIDTH, (int)(0.15*Game.HEIGHT));
        }

        public void update(float delta, float currentSpeed) {
            land1.xPos -= Math.floor(currentSpeed * delta * Game.FPS);
            land2.xPos -= Math.floor(currentSpeed * delta * Game.FPS);
            if (land1.xPos <= -land1.width)
                land1.xPos = land2.xPos + land1.width;
            if (land2.xPos <= -land1.width)
                land2.xPos = land1.xPos + land1.width;
        }

        public void paint(Canvas canvas, boolean waiting, boolean running) {
            if (!waiting) {
                if (!running) {
                    land1.paint(canvas);
                }
                if (running) {
                    land1.paint(canvas);
                    land2.paint(canvas);
                }
            } else {
                land1.paint(canvas);
            }
        }
    }
}
