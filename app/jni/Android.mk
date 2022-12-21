LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := return_data
LOCAL_SRC_FILES := ReturnData.cpp

include $(BUILD_SHARED_LIBRARY)