#include <stdlib.h>
#include <jni.h>
#include <limits.h>
#include <time.h>
#include <x86intrin.h>
#include <immintrin.h>
#include "arraySum_arraySum.h"

JNIEXPORT void JNICALL Java_arraySum_arraySum_sumJNIcpy
(JNIEnv *env, jclass clazz, jdoubleArray a, jint offseta, jdoubleArray b, jint offsetb, jint length) {

	jlong result;
	double* basea = malloc(2 * length * sizeof(double));
	(*env)->GetDoubleArrayRegion(env, a, offseta,length, basea);
	double* baseb = basea + length;
	(*env)->GetDoubleArrayRegion(env, b, offsetb,length, baseb);

	for(int i = 0; i < length; ++i)
	basea[i] += baseb[i];

	(*env)->SetDoubleArrayRegion(env, a, offseta,length, basea);
	free(basea);

}

JNIEXPORT void JNICALL Java_arraySum_arraySum_sumJNI
(JNIEnv *env, jclass clazz, jdoubleArray a, jint offseta, jdoubleArray b, jint offsetb, jint length) {

	double* basea = (*env)->GetPrimitiveArrayCritical(env, a, 0);
	double* baseb = (*env)->GetPrimitiveArrayCritical(env, b, 0);

	double *ashift = &(basea[offseta]);
	double *bshift = &(baseb[offsetb]);

	for(int i = 0; i < length; ++i)
	ashift[i] += bshift[i];

	(*env)->ReleasePrimitiveArrayCritical(env, a, basea, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, b, baseb, 0);

}

JNIEXPORT void JNICALL Java_arraySum_arraySum_sumJNIAVX
(JNIEnv *env, jclass clazz, jdoubleArray a, jint offseta, jdoubleArray b, jint offsetb, jint length) {

	double* basea = (*env)->GetPrimitiveArrayCritical(env, a, 0);
	double* baseb = (*env)->GetPrimitiveArrayCritical(env, b, 0);

	double *ashift = &(basea[offseta]);
	double *bshift = &(baseb[offsetb]);

	int bound = length - 4;

	int i = 0;

	for (; i < bound; i += 4) {
		__m256d pa, pb, psum;
		pa = _mm256_loadu_pd(&(ashift[i]));
		pb = _mm256_loadu_pd(&(bshift[i]));
		psum = _mm256_add_pd(pa, pb);
		_mm256_storeu_pd(&(ashift[i]), psum);
	}

	for (; i < length; ++i)
	ashift[i] += bshift[i];

	(*env)->ReleasePrimitiveArrayCritical(env, a, basea, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, b, baseb, 0);

}

JNIEXPORT void JNICALL Java_arrayProd_arraySum_sumnJNI
(JNIEnv *env, jclass clazz, jlong a, jint offseta,jlong b, jint offsetb, jint length) {

	double* basea = ((double*)a) + offseta;
	double* baseb = ((double*)b) + offsetb;

	for(int i = 0; i < length; ++i)
	basea[i] += baseb[i];

}

JNIEXPORT void JNICALL Java_arrayProd_arraySum_sumnJNIAVXu
(JNIEnv *env, jclass clazz, jlong a, jint offseta,jlong b, jint offsetb, jint length) {

	double* ashift = ((double*)a) + offseta;
	double* bshift = ((double*)b) + offsetb;

	int bound = length - 4;

	int i = 0;

	for (; i < bound; i += 4) {
		__m256d pa, pb, psum;
		pa = _mm256_loadu_pd(&(ashift[i]));
		pb = _mm256_loadu_pd(&(bshift[i]));
		psum = _mm256_add_pd(pa, pb);
		_mm256_storeu_pd(&(ashift[i]), psum);
	}

	for (; i < length; ++i)
	ashift[i] += bshift[i];

}

