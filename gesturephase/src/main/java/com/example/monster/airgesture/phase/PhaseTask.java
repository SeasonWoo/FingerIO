package com.example.monster.airgesture.phase;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.monster.airgesture.PhaseProcessI;
import com.example.monster.airgesture.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;

/**
 * 录音数据解析处理模块(native层{@link PhaseTask#mPPI})代理
 */

class PhaseTask {
    private static final String KEY_SENSITIVITY = "KEY_SENSITIVITY";
    private static final int MESSAGE_PHASE_MODEL = 0x100;
    private static final String TYPE = "type";

    private PhaseProcessI mPPI;
    private IPhaseBiz.PhaseListener mListener;
    private Handler mHandler;
    private Context mContext;
    private BlockingQueue<short[]> mQueue;

    private boolean isReadyRunning;

    PhaseTask(Context context) {
        mContext = context;
        mPPI = new PhaseProcessI();
        mPPI.doInit(mPPI.nativeSignalProcess, Condition.S_FILE_TEMPLATE_PATH);
    }

    void modifySensitivity(int sensitivity) {
        SPUtils.put(KEY_SENSITIVITY, sensitivity, mContext);
    }

    void start(BlockingQueue<short[]> queue, IPhaseBiz.PhaseListener listener) {
        this.mListener = listener;
        this.mQueue = queue;
        //处理从线程回传回来的数据
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == MESSAGE_PHASE_MODEL) {
                    int type = msg.getData().getInt(TYPE);
                    switch (type) {
                        case 1:
                            mListener.receiveActionType(Gesture.HENG);
                            break;
                        case 2:
                            mListener.receiveActionType(Gesture.SHU);
                            break;
                        case 3:
                            mListener.receiveActionType(Gesture.ZUO_XIE);
                            break;
                        case 4:
                            mListener.receiveActionType(Gesture.YOU_XIE);
                            break;
                        case 5:
                            mListener.receiveActionType(Gesture.ZUO_HU);
                            break;
                        case 6:
                            mListener.receiveActionType(Gesture.YOU_HU);
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });

        //开启子线程处理逻辑
        isReadyRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                recognizeGesture();
            }
        }).start();
    }

    void stop() {
        isReadyRunning = false;
    }

    /**
     * 解析音频数据获得手势类型
     */
    private void recognizeGesture() {
        int sensitive = (int) SPUtils.get(KEY_SENSITIVITY, 50, mContext);
        String sFileName = getRecordedFileName("jni");
        while (isReadyRunning) {
            //从录制音频数据队列获取待解析音频数据
            try {
//                short[] recData = mQueue.take();
                short[] recData = mQueue.poll();
                if (recData != null && recData.length > 0) {
                    float iType = mPPI.doActionRecognitionV3(mPPI.nativeSignalProcess, recData,
                            recData.length, Condition.S_FILE_RESULT_PATH, sFileName, sensitive);
                    if (iType > 0.0f) {
                        //Log.i("PhaseTask", "receive : " + iType);
                        Bundle bundle = new Bundle();
                        bundle.putInt(TYPE, (int) iType);
                        Message message = Message.obtain(mHandler, MESSAGE_PHASE_MODEL);
                        message.setData(bundle);
                        message.sendToTarget();
                    }
                }
//            } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getRecordedFileName(String extensionName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH'h'-mm'm'-ss's'");
        return extensionName + df.format(System.currentTimeMillis());
    }
}
