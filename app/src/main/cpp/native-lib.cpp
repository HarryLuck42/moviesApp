//
// Created by Hariyanto Lukman on 25/12/2021.
//
#include <jni.h>
#include <string>
#include <iostream>
using namespace std;

const int ENV_PRODUCTION = 1;
const int ENV_STAGING = 2;
const int ENV_QA = 3;

extern "C" {


JNIEXPORT jstring JNICALL
Java_com_corp_luqman_movielisting_utils_Const_appUrl(
        JNIEnv *env, jobject, jint envtype){
    string appProUr;
    if (envtype == ENV_PRODUCTION) {
        appProUr = "https://api.themoviedb.org/3/";
    } else if (envtype == ENV_STAGING) {
        appProUr = "";
    } else if (envtype == ENV_QA){
        appProUr = "";
    }else{
        appProUr = "";
    }
    return env->NewStringUTF(appProUr.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_corp_luqman_movielisting_utils_Const_imageUrl(
        JNIEnv *env, jobject, jint envtype){
    string appProUr;
    if (envtype == ENV_PRODUCTION) {
        appProUr = "http://image.tmdb.org/t/p/w220_and_h330_face";
    } else if (envtype == ENV_STAGING) {
        appProUr = "";
    } else if (envtype == ENV_QA){
        appProUr = "";
    }else{
        appProUr = "";
    }
    return env->NewStringUTF(appProUr.c_str());
}


}

