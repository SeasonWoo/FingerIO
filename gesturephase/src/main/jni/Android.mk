LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS)
OpenCV_INSTALL_MODULES := on
OpenCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC

ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
    include $(LOCAL_PATH)/native/jni/OpenCV.mk
else
    include $(OPENCV_MK_PATH)
endif
LOCAL_MODULE := PhaseProcess
LOCAL_SRC_FILES := com_magicing_eigenndk_NDKUtils.cpp MatrixProcess.cpp RangeFinder.cpp PhaseProcess.cpp Biquad.cpp Butterworth.cpp  ButterworthFilter.cpp spectrogram.cpp \
 GammaUtil.cpp bwareaopen.cpp  dtwrecogeopt.cpp interpolate.cpp SignalProcess.cpp  segAction.cpp

LOCAL_LDLIBS +=  -lm -llog
include $(BUILD_SHARED_LIBRARY)

