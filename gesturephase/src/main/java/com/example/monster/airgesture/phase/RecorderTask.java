package com.example.monster.airgesture.phase;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;


import java.util.concurrent.BlockingQueue;

/**
 * 录制声音
 */
class RecorderTask {

    private static final String TAG = "RecorderTask";

    private boolean isReadyRecording;

    private AudioRecord mAudioRecord;
    private int mMinBufferSize;

    /*RecorderTask(AudioRecord audioRecord, int bufferSize) {
        this.mAudioRecord = audioRecord;
        this.mMinBufferSize = bufferSize;
    }*/

    RecorderTask(int channelIn, int sampleRate, int encoding) {
        mMinBufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                channelIn,
                encoding);
        mAudioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelIn,
                encoding,
                mMinBufferSize);
    }

    public void start(final BlockingQueue<short[]> queue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                short[] buffer = new short[mMinBufferSize];
                mAudioRecord.startRecording();
                isReadyRecording = true;
                while (isReadyRecording) {
                    int readResult = mAudioRecord.read(buffer, 0, mMinBufferSize);
                    if (readResult == AudioRecord.ERROR_INVALID_OPERATION) {
                        Log.e(TAG, "readState == AudioRecord.ERROR_INVALID_OPERATION");
                        return;
                    } else if (readResult == AudioRecord.ERROR_BAD_VALUE) {
                        Log.e(TAG, "readState == AudioRecord.ERROR_BAD_VALUE");
                        return;
                    }
                    try {
                        queue.put(buffer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        if (mAudioRecord != null) {
            mAudioRecord.stop();
            mAudioRecord.release();
        }
        isReadyRecording = false;
    }
}
