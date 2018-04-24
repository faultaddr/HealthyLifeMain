#include <cstring>
#include <jni.h>

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc_c.h>
#include <opencv2/opencv.hpp>


using namespace std;
using namespace cv;

//
// Created by panyunyi on 2018/4/24.
//

extern "C"
JNIEXPORT void JNICALL
Java_cn_panyunyi_healthylife_app_main_ui_activity_MonitorActivity_yuv420sp_1to_1yuv420p(JNIEnv *env,
                                                                                        jobject instance,
                                                                                        jbyteArray yuv420sp_,
                                                                                        jbyteArray yuv420p_,
                                                                                        jint width,
                                                                                        jint height) {
    jbyte *yuv420sp = env->GetByteArrayElements(yuv420sp_, NULL);
    jbyte *yuv420p = env->GetByteArrayElements(yuv420p_, NULL);

    // TODO

    int PixelsCount  = width * height;
    int i = 0, j= 0;

    if (yuv420sp== NULL || yuv420p == NULL)
    {
        return;
    }

    //copy  Y
    for (i = 0; i < PixelsCount;  i++)
    {
        *(yuv420p +i) = *(yuv420sp + i);
    }

    //copy  Cb(U)
    i = 0;
    for (j = 0;  j < PixelsCount / 2;  j+=2)
    {
        *(yuv420p + (i + PixelsCount)) = *(yuv420sp + (j + PixelsCount));
        i++;
    }

    //copy Cr(V)
    i = 0;
    for(j = 1;  j < PixelsCount/2;  j+=2)
    {
        *(yuv420p +(i+PixelsCount * 5/4)) = *(yuv420sp + (j + PixelsCount));
        i++;
    }


    env->ReleaseByteArrayElements(yuv420sp_, yuv420sp, 0);
    env->ReleaseByteArrayElements(yuv420p_, yuv420p, 0);
}
Mat * mCanny = NULL;
extern "C"
JNIEXPORT jdouble JNICALL
Java_cn_panyunyi_healthylife_app_main_util_ImageProcessing_decodeYUV420spToGrey(  JNIEnv* env, jobject thiz,
                                                                                  jint width, jint height,
                                                                                  jbyteArray NV21FrameData, jintArray outPixels)
{
    jbyte * pNV21FrameData = env->GetByteArrayElements(NV21FrameData, 0);  //输入yuv数据
    jint * poutPixels = env->GetIntArrayElements(outPixels, 0);  //输出结果的int数据
    if ( mCanny == NULL )
    {
        mCanny = new Mat(height, width, CV_8UC1);
    }
    Mat mGray(height, width, CV_8UC1, (unsigned char *)pNV21FrameData); //构建灰度图时构造函数
    Mat mResult(height, width, CV_8UC4, (unsigned char *)poutPixels);
    IplImage srcImg = mGray;
    IplImage CannyImg = *mCanny;
    IplImage ResultImg = mResult;
    CvScalar scalar = cvAvg(&srcImg);
    cvCanny(&srcImg, &CannyImg, 80, 100, 3);
    cvCvtColor(&CannyImg, &ResultImg, CV_GRAY2BGRA);
    env->ReleaseByteArrayElements(NV21FrameData, pNV21FrameData, 0);
    env->ReleaseIntArrayElements(outPixels, poutPixels, 0);
    return scalar.val[0];
}