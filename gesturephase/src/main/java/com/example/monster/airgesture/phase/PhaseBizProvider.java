package com.example.monster.airgesture.phase;

import android.content.Context;
import android.media.AudioFormat;

/**
 * 依赖注入
 */

public class PhaseBizProvider {
    private static final int AUDIO_RECORDER_SAMPLE_RATE = 44100;//录制采样率
    private static final int AUDIO_RECORDER_CHANNEL = AudioFormat.CHANNEL_IN_MONO;//录音通道
    private static final int AUDIO_RECORDER_ENCODING = AudioFormat.ENCODING_PCM_16BIT;//编码方式
    private static final int AUDIO_PLAY_SAMPLE_RATE = 44100;//播放采样率
    private static final int AUDIO_PLAY_FREQ = 20000;//音频频率采样率，音频播放频率

    /**
     * 提供 IPhaseBiz 实例对象
     */
    public static IPhaseBiz providePhaseBiz(Context context) {
        return providePhaseBiz(context,
                AUDIO_RECORDER_SAMPLE_RATE,
                AUDIO_RECORDER_CHANNEL,
                AUDIO_RECORDER_ENCODING,
                AUDIO_PLAY_SAMPLE_RATE,
                AUDIO_PLAY_FREQ);
    }

    public static IPhaseBiz providePhaseBiz(Context context,
                                            int recordSampleRate,
                                            int recordChannel,
                                            int recordEncoding,
                                            int playSampleRate,
                                            int playFreq) {
        return new PhaseBiz(
                context,
                new PhaseTask(context),
                new HighFreqWavePlayTask(playFreq, playSampleRate),
                new RecorderTask(recordChannel, recordSampleRate, recordEncoding));
    }
}
