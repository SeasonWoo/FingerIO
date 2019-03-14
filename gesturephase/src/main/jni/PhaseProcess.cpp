//
// Created by bostinshi on 2017/7/27.
//
#include "com_example_monster_airgesture_PhaseProcessI.h"
#include <RangeFinder.h>
#include <SignalProcess.h>

JNIEXPORT jlong
JNICALL Java_com_example_monster_airgesture_PhaseProcessI_createNativeSignalProcess
        (JNIEnv *env, jobject obj) {
    return (jlong)(new SignalProcess());
}


JNIEXPORT jfloat
JNICALL Java_com_example_monster_airgesture_PhaseProcessI_doActionRecognitionV3
        (JNIEnv *env, jobject obj, jlong thizptr, jshortArray recordData,
        jint size, jstring j_str, jstring j_str2, jint sensitive) {

    jshort * carr;
    carr = env->GetShortArrayElements(recordData, 0);
    if (carr == NULL) {
        return 0;
    }

    const char* c_str;
    c_str = env->GetStringUTFChars(j_str, NULL);
    const char* c_str2;
    c_str2 = env->GetStringUTFChars(j_str2, NULL);
    int iType = ((SignalProcess *) thizptr)->doProcessV3(carr, size, c_str, c_str2, sensitive);
    //doActionRecognitionV3对应的C实现
    //fDistance = RangeFinder::getCosin();//addAll(carr, size);
    env->ReleaseShortArrayElements(recordData, carr, 0);
    env->ReleaseStringUTFChars(j_str, c_str);
    env->ReleaseStringUTFChars(j_str2, c_str2);

    return iType;
}



JNIEXPORT jfloat
JNICALL Java_com_example_monster_airgesture_PhaseProcessI_doInit
        (JNIEnv *env, jobject obj, jlong thizptr, jstring j_str) {
    jfloat iRet = 0;
    const char* c_str;
    c_str = env->GetStringUTFChars(j_str, NULL);

    iRet = ((SignalProcess *) thizptr)->init(c_str);
    env->ReleaseStringUTFChars(j_str, c_str);

    return iRet;
}
