#include <jni.h>

#ifndef _INCLUDED_DTW
#define  _INCLUDED_DTW

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jfloat JNICALL
        Java_com_example_ida_dronecontrol_CalculVocal_dtw(JNIEnv *, jobject );
#ifdef __cplusplus
}

#endif

#endif