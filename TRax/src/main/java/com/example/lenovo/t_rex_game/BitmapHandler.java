//package com.example.lenovo.t_rex_game;
//
//import android.os.Bundle;
//import android.os.Looper;
//import android.os.Message;
//
//import java.util.logging.Handler;
//
//public class BitmapHandler extends Handler {
//    public BitmapHandler() {
//    }
//
//    ;
//
//    public BitmapHandler(Looper L) {
//        super(L);
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//        // 这里用于更新UI
//        Bundle b = msg.getData();
//        String color = b.getString("color");
//        Game.this.textView.setText(color);
//    }
//}
