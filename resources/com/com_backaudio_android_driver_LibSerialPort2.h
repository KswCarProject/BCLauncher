/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_backaudio_android_driver_LibSerialPort2 */

#ifndef _Included_com_backaudio_android_driver_LibSerialPort2
#define _Included_com_backaudio_android_driver_LibSerialPort2
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_backaudio_android_driver_LibSerialPort2
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_backaudio_android_driver_LibSerialPort2_open
  (JNIEnv *, jobject, jstring, jint, jint);

/*
 * Class:     com_backaudio_android_driver_LibSerialPort2
 * Method:    close
 * Signature: (Ljava/io/FileDescriptor;)V
 */
JNIEXPORT void JNICALL Java_com_backaudio_android_driver_LibSerialPort2_close
  (JNIEnv *, jobject, jobject);

/*
 * Class:     com_backaudio_android_driver_LibSerialPort2
 * Method:    read
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_com_backaudio_android_driver_LibSerialPort2_read
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     com_backaudio_android_driver_LibSerialPort2
 * Method:    write
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_backaudio_android_driver_LibSerialPort2_write
  (JNIEnv *, jobject, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
