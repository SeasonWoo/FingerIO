package com.example.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.nightonke.wowoviewpager.Animation.WoWoTextViewTextAnimation;

import me.wangyuwei.particleview.ParticleView;

public class StartupActivity extends AppCompatActivity {
    ParticleView mPvGithub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_startup);
        mPvGithub = findViewById(R.id.pv_github);

        mPvGithub.startAnim();
        mPvGithub.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Intent intent = new Intent(StartupActivity.this, WoWoTextViewTextAnimation.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
