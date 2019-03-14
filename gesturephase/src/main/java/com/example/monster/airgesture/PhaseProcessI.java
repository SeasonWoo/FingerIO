package com.example.monster.airgesture;

/**
 * Created by bostinshi on 2017/7/27.
 * <p>
 * 不要改变这个类的位置，否则会报错
 */

public class PhaseProcessI {

    static {
        System.loadLibrary("PhaseProcess");  //导入C代码生成的二进制库
    }

    //保存c++类的地址
    public long nativeSignalProcess;

    //构造函数
    public PhaseProcessI() {
        nativeSignalProcess = createNativeSignalProcess();
    }

    public native long createNativeSignalProcess();

    public native float doActionRecognitionV3(long thizptr, short[] recordData, int iLen, String sPath, String sName, int sensitive);

    public native float doInit(long thizptr, String sPath);
}
