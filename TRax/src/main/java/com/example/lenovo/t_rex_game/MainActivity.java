package com.example.lenovo.t_rex_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public ImageView iv_canvas;
    private Bitmap land;
    private Bitmap cloud;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private Bitmap originalBitmap;
    private int phoneW,phoneH;
    private int alfa; //雪碧图与屏幕的比例；
    private static final String TAG = "MainActivity";


    float startX;
    float startY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);

        iv_canvas = findViewById(R.id.iv_canvas);
        originalBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sprites);


        land = Bitmap.createBitmap(originalBitmap, 2, 320, 1200*2, originalBitmap.getHeight()-320);
        cloud = Bitmap.createBitmap(originalBitmap, 490, 0, 200, 100);
        Log.d(TAG, "onCreate: the size: width "+originalBitmap.getWidth()
                +" height "+originalBitmap.getHeight());

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.d(TAG, "onCreate: the phone size: width "+dm.widthPixels+" height "+dm.heightPixels);
        phoneW = dm.widthPixels;
        phoneH = dm.heightPixels;
        alfa = (int)Math.ceil(originalBitmap.getWidth()/phoneW);

        bitmap = Bitmap.createBitmap( phoneW, phoneH, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#f7f7f7"));

        drawImage(canvas, originalBitmap, 0, phoneH-140
                ,2,288, 2200*2, 70, 2);
        iv_canvas.setImageBitmap(bitmap);


    }

    public void drawImage(Canvas canvas, Bitmap blt, int bx, int by,
                                  int sx, int sy, int sw, int sh, int alfa) {
        if(blt != null){
            Log.d(TAG, "drawImage: true");
        }else{
            Log.d(TAG, "drawImage: false");
        }
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形

        src.left = sx;
        src.top = sy;
        src.right = sx + sw;
        src.bottom = sy + sh;

        dst.left = bx;
        dst.top = by;
        dst.right = bx + sw/alfa;
        dst.bottom = by + sh*alfa;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, src, dst, null);

        src = null;
        dst = null;
    }
    public void drawImage(Canvas canvas, Bitmap blt, int bx, int by,
                          int bw, int bh, int sx, int sy, int sw, int sh) {
        if(blt != null){
            Log.d(TAG, "drawImage: true");
        }else{
            Log.d(TAG, "drawImage: false");
        }
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形

        src.left = sx;
        src.top = sy;
        src.right = sx + sw;
        src.bottom = sy + sh;

        dst.left = bx;
        dst.top = by;
        dst.right = bx + bw;
        dst.bottom = by + bh;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, src, dst, null);

        src = null;
        dst = null;
    }
}
