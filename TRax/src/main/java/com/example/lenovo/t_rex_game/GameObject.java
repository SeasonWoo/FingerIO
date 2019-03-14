package com.example.lenovo.t_rex_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.LinkedList;

public class GameObject {
    private static final String TAG = "GameObject";
    int width, height;
    public float xPos, yPos;
    int sourceX, sourceY, sourceWidth, sourceHeight;
    Bitmap bitmap;
    LinkedList<Rect> rects;
    Rect originalRect;

    public GameObject(int sourceX, int sourceY, int sourceWidth, int sourceHeight, float xPos, float yPos, int width, int height) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceWidth = sourceWidth; //目标宽
        this.sourceHeight = sourceHeight; //目标高
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        getBitmap();
        rects = new LinkedList<>();
    }
    public GameObject(GameImage img,int sourceX, int sourceY, int sourceWidth, int sourceHeight, float xPos, float yPos, int width, int height) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceWidth = sourceWidth; //目标宽
        this.sourceHeight = sourceHeight; //目标高
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        getBitmap(img);
        getBitmap1();
        rects = new LinkedList<>();
    }
    public GameObject(GameImage img, float xPos, float yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        getBitmap(img);
        rects = new LinkedList<>();
    }

    private void getBitmap() {
        Bitmap originalBitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.sprites);
        bitmap = Bitmap.createBitmap(originalBitmap, sourceX, sourceY, sourceWidth, sourceHeight);
    }
    public void getBitmap(int sourceX, int sourceY, int sourceWidth, int sourceHeight){
        bitmap = Bitmap.createBitmap(bitmap, sourceX, sourceY, sourceWidth, sourceHeight);
    }
    private void getBitmap1() {
        bitmap = Bitmap.createBitmap(bitmap, sourceX, sourceY, sourceWidth, sourceHeight);
    }
    private void getBitmap(GameImage img){
        switch (img){
            case bird_down:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.bird_down);
                break;
            case bird_up:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.bird_up);
                break;
            case cactus_big:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.cactus_big);
                break;
            case cactus_small:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.cactus_small);
                break;
            case cloud:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.cloud);
                break;
            case gameover:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.gameover);
                break;
            case land:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.land);
                break;
            case numbers:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.numbers);
                break;
            case regame:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.regame);
                break;
            case sprites:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.sprites);
                break;
            case trax:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.trax);
                break;
            case trax_duck:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.trax_duck);
                break;
            case trax_waiting:
                bitmap = BitmapFactory.decodeResource(Game.resourcesInstance, R.drawable.trax_waiting);
                break;
        }
    }

    public void paint(Canvas canvas) {
        Rect rect = new Rect();
        rect.left = (int) xPos;
        rect.top = (int) yPos;
        rect.right = (int) (xPos + width);
        rect.bottom = (int) (yPos + height);
        canvas.drawBitmap(bitmap, null, rect, null);
    }


    void addRects(int x, int y, int width, int height) {
        rects.addLast(new Rect(x, y, x + width, y + height));
    }

    void setOriginalRect() {
        originalRect = new Rect((int) (xPos + 1), (int) (yPos + 1)
                , (int)(xPos + width - 1), (int)(yPos + height - 1));
    }


    Rect creatAdjustedRect(Rect ob1, Rect ob2) {
        //新生成矩形的长宽为ob2的长宽
        return new Rect(ob1.left + ob2.left, ob1.top + ob2.top
                , ob2.right+ob1.left, ob2.bottom+ob1.top);
    }

}
