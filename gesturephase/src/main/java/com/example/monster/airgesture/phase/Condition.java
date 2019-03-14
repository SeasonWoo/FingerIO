package com.example.monster.airgesture.phase;

import android.os.Environment;

class Condition {
    static final String S_ABSOLUTE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/files/phase/";  //存放数据的绝对路径
    static final String S_FILE_TEMPLATE_PATH = S_ABSOLUTE_PATH + "/template/";
    static final String S_FILE_RESULT_PATH = S_ABSOLUTE_PATH + "/result/";
}
