package com.richard.echotypewear;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.example.monster.airgesture.phase.Gesture;
import com.example.monster.airgesture.phase.IPhaseBiz;
import com.example.monster.airgesture.phase.PhaseBizProvider;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private BoxInsetLayout mBoxInsetLayout;

    private IPhaseBiz mPhaseBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);
        mBoxInsetLayout = findViewById(R.id.box_layout);
        mPhaseBiz = PhaseBizProvider.providePhaseBiz(MainActivity.this);
        mPhaseBiz.startRecognition(new IPhaseBiz.PhaseListener() {
            @Override
            public void receiveActionType(Gesture type) {
                mTextView.setText(type.getStoke());
                if (type.equals(Gesture.HENG)) {
                    mBoxInsetLayout.setBackgroundColor(Color.RED);
                } else if (type.equals(Gesture.SHU)) {
                    mBoxInsetLayout.setBackgroundColor(Color.BLUE);
                } else if (type.equals(Gesture.ZUO_XIE)) {
                    mBoxInsetLayout.setBackgroundColor(Color.GREEN);
                } else if (type.equals(Gesture.YOU_XIE)) {
                    mBoxInsetLayout.setBackgroundColor(Color.YELLOW);
                } else if (type.equals(Gesture.ZUO_HU)) {
                    mBoxInsetLayout.setBackgroundColor(Color.CYAN);
                } else if (type.equals(Gesture.YOU_HU)) {
                    mBoxInsetLayout.setBackgroundColor(Color.MAGENTA);
                }
            }
        });
        setAmbientEnabled();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhaseBiz.stopRecognition();
    }
}
