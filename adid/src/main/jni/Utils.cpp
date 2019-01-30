#include <jni.h>

//
// Created by Gin on 2019/1/25.
//
extern "C" JNIEXPORT jstring JNICALL Java_com_onemt_adid_JNIUtil_getAndroidId(JNIEnv *env, jobject instance, jobject activity) {
    // get contentResolver
//    jclass activity = env->GetObjectClass(instance);
    jclass clz = env->GetObjectClass(activity);
    jmethodID method = env->GetMethodID(clz, "getContentResolver", "()Landroid/content/ContentResolver;");
    jobject resolverInstance = env->CallObjectMethod(activity, method);
    // get android_id from android Settings$Secure
    jclass androidSettingsClass = env->FindClass("android/provider/Settings$Secure");
    jmethodID methodId = env->GetStaticMethodID(androidSettingsClass, "getString",
                                                "(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;");
    jstring param_android_id = env->NewStringUTF("android_id");
    jstring android_id = (jstring) env->CallStaticObjectMethod(androidSettingsClass, methodId, resolverInstance,
                                                               param_android_id);
    // get serial num from android Build
    jclass androidBuildClass = env->FindClass("android/os/Build");
    jfieldID SERIAL = env->GetStaticFieldID(androidBuildClass, "SERIAL", "Ljava/lang/String;");
    jstring serialNum = (jstring) env->GetStaticObjectField(androidBuildClass,
                                                            SERIAL);
    // concat strings
    jclass String_clazz = env->FindClass("java/lang/String");
    jmethodID concat_methodID = env->GetMethodID(String_clazz, "concat", "(Ljava/lang/String;)Ljava/lang/String;");
    jstring result = (jstring) env->CallObjectMethod(android_id, concat_methodID,
                                                     serialNum);
    // release
    env->DeleteLocalRef(param_android_id);
    env->DeleteLocalRef(serialNum);
    return result;
}
