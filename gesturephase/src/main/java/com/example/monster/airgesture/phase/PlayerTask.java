package com.example.monster.airgesture.phase;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * 使用播放器发出指定频率{@link PlayerTask#mWaveRate}的正弦波
 */

class PlayerTask {
    private int mWaveRate;
    private int mSampleRate;
    private AudioTrack mAudioTrack;
    private int mMinBufferSize;

    PlayerTask(int waveRate, int sampleRate) {
        mWaveRate = waveRate;
        mSampleRate = sampleRate;
        mMinBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                mMinBufferSize,
                AudioTrack.MODE_STREAM);
    }

    /*PlayerTask(int waveRate,int sampleRate, AudioTrack audioTrack, int bufferSize) {
        this.mWaveRate = waveRate;
        this.mSampleRate = sampleRate;
        this.mAudioTrack = audioTrack;
        this.mMinBufferSize = bufferSize;
    }*/

    public void start() {
        mAudioTrack.play();
        new Thread(new Runnable() {
            @Override
            public void run() {
                double sampleCountInWave = mSampleRate / (double) mWaveRate;//每一个波中，包含的样本点数量
                short[] wave = new short[mMinBufferSize];
                int index = 0;
                while (mAudioTrack != null && mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                    for (int i = 0; i < wave.length; ++i, ++index) {
                        wave[i] = (short) (Short.MAX_VALUE / 2 *
                                Math.sin(2.0 * Math.PI * index / sampleCountInWave)
                        );
                    }
                    mAudioTrack.write(wave, 0, mMinBufferSize);
                }
            }
        }).start();
    }

    public void stop() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
    }
}
