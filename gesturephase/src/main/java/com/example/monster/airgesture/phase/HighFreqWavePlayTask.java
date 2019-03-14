package com.example.monster.airgesture.phase;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * 使用播放器发出指定频率{@link HighFreqWavePlayTask#mWaveRate}的正弦波
 */

class HighFreqWavePlayTask {
    private int mWaveRate;
    private int mSampleRate;
    private AudioTrack mAudioTrack;
    private int mMinBufferSize;

    HighFreqWavePlayTask(int waveRate, int sampleRate) {
        mWaveRate = waveRate;
        mSampleRate = sampleRate;
        mMinBufferSize = 3*AudioTrack.getMinBufferSize(
//        mMinBufferSize = AudioTrack.getMinBufferSize(
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

    /*HighFreqWavePlayTask(int waveRate,int sampleRate, AudioTrack audioTrack, int bufferSize) {
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
