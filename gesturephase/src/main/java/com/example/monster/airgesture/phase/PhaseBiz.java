package com.example.monster.airgesture.phase;

import android.content.Context;

import com.example.monster.airgesture.utils.FileCopyUtils;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;


/**
 * 手势解析模块实现类
 */

public class PhaseBiz implements IPhaseBiz {

    private PhaseTask mPhaseTask;
    private HighFreqWavePlayTask mHighFreqWavePlayTask;
    private RecorderTask mRecorderTask;
    private Context mContext;

    private boolean isRunning;

    PhaseBiz(Context context, PhaseTask mPhaseTask,
             HighFreqWavePlayTask mHighFreqWavePlayTask, RecorderTask mRecorderTask) {
        this.mPhaseTask = mPhaseTask;
        this.mHighFreqWavePlayTask = mHighFreqWavePlayTask;
        this.mRecorderTask = mRecorderTask;
        this.mContext = context;
        File fAbsolutePath = new File(Condition.S_ABSOLUTE_PATH);
        File fTemplatePath = new File(Condition.S_FILE_TEMPLATE_PATH);
        File fResultPath = new File(Condition.S_FILE_RESULT_PATH);
        //创建文件夹
        if (!fAbsolutePath.exists()) fAbsolutePath.mkdirs();
        if (!fTemplatePath.exists()) fTemplatePath.mkdirs();
        if (!fResultPath.exists()) fResultPath.mkdirs();
        //拷贝Assets下的模板数据
        copyTemplate("heng2.txt");
        copyTemplate("shu2.txt");
        copyTemplate("youhu2.txt");
        copyTemplate("youxie2.txt");
        copyTemplate("zuohu2.txt");
        copyTemplate("zuoxie2.txt");
    }

    @Override
    public void startRecognition(PhaseListener listener) {
        BlockingQueue<short[]> queue = new LinkedTransferQueue<>();
        if (!isRunning) {
            mHighFreqWavePlayTask.start();
            mRecorderTask.start(queue);
            mPhaseTask.start(queue, listener);
            isRunning = true;
        }
    }

    @Override
    public void stopRecognition() {
        if (isRunning) {
            mHighFreqWavePlayTask.stop();
            mRecorderTask.stop();
            mPhaseTask.stop();
            isRunning = false;
        }
    }

    @Override
    public void modifySensitivity(int sensitivity) {
        mPhaseTask.modifySensitivity(sensitivity);
    }

    /**
     * 拷贝模板数据
     */
    private void copyTemplate(String templateName) {
        FileCopyUtils.copyInAssets(templateName,
                Condition.S_FILE_TEMPLATE_PATH + templateName, mContext);
    }
}
