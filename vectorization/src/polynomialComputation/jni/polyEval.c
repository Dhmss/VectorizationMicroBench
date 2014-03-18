#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <limits.h>
#include <x86intrin.h>
#include <immintrin.h>
#include "polyEval.h"

const JNINativeMethod JNI_METHODS[] = {

{ "jni_avxu", "([D[D[DII)V", (void *) &jni_avxu }, { "jni_basic", "([D[D[DII)V",
		(void *) &jni_basic }, { "jni_avxa", "([D[D[DII)V", (void *) &jni_avxa },
		{ "native_jni_avxu", "([DJJII)V", (void *) &native_jni_avxu }

};

const int NB_JNI_METHODS = 4;

JNIEXPORT void JNICALL
Java_polynomialComputation_polyEval_registerNatives(JNIEnv *env, jclass cls) {
	(*env)->RegisterNatives(env, cls, JNI_METHODS, NB_JNI_METHODS);
}

JNIEXPORT void JNICALL
jni_basic(JNIEnv *env, jclass cls, jdoubleArray jcoef, jdoubleArray jx,
		jdoubleArray jy, jint degree, jint vectorSize) {

	double* coef = (*env)->GetPrimitiveArrayCritical(env, jcoef, 0);
	double* x = (*env)->GetPrimitiveArrayCritical(env, jx, 0);
	double* y = (*env)->GetPrimitiveArrayCritical(env, jy, 0);

	for (int i = 0; i < vectorSize; ++i)
		y[i] = coef[degree];

	for (int d = degree - 1; d > -1; --d) {
		int offset = coef[d];
		for (int i = 0; i < vectorSize; ++i) {
			y[i] *= x[i];
			y[i] += offset;
		}
	}

	(*env)->ReleasePrimitiveArrayCritical(env, jcoef, coef, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, jx, x, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, jy, y, 0);
}

JNIEXPORT void JNICALL
jni_avxu(JNIEnv *env, jclass cls, jdoubleArray jcoef, jdoubleArray jx,
		jdoubleArray jy, jint degree, jint vectorSize) {

	double* coef = (*env)->GetPrimitiveArrayCritical(env, jcoef, 0);
	double* x = (*env)->GetPrimitiveArrayCritical(env, jx, 0);
	double* y = (*env)->GetPrimitiveArrayCritical(env, jy, 0);

	int i;
	for (i = 0; i < vectorSize; ++i)
		y[i] = coef[degree];
	int boundavx = vectorSize - 4;
	for (int d = degree - 1; d > -1; --d) {
		double offset = coef[d];
		__m256d packedy, packedx;
		__m256d offsets = _mm256_broadcast_sd(&offset);
		for (i = 0; i < boundavx; i += 4) {
			double* yi = &(y[i]);
			double* xi = &(x[i]);
			packedx = _mm256_loadu_pd(xi);
			packedy = _mm256_loadu_pd(yi);
			packedy = _mm256_mul_pd(packedy, packedx);
			packedy = _mm256_add_pd(packedy, offsets);
			_mm256_storeu_pd(yi, packedy);
		}
		for (; i < vectorSize; ++i) {
			y[i] *= x[i];
			y[i] += offset;
		}
	}

	(*env)->ReleasePrimitiveArrayCritical(env, jcoef, coef, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, jx, x, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, jy, y, 0);
}

JNIEXPORT void JNICALL
jni_avxa(JNIEnv *env, jclass cls, jdoubleArray jcoef, jdoubleArray jx,
		jdoubleArray jy, jint degree, jint vectorSize) {

	double* coef = (*env)->GetPrimitiveArrayCritical(env, jcoef, 0);

	double* xu = (*env)->GetPrimitiveArrayCritical(env, jx, 0);
	double* yu = (*env)->GetPrimitiveArrayCritical(env, jy, 0);
	double* x = &(xu[6]);
	double* y = &(yu[6]);

	int i;
	for (i = 0; i < vectorSize; ++i)
		y[i] = coef[degree];
	int boundavx = vectorSize - 4;
	for (int d = degree - 1; d > -1; --d) {
		double offset = coef[d];
		__m256d packedy, packedx;
		__m256d offsets = _mm256_broadcast_sd(&offset);
		for (i = 0; i < boundavx; i += 4) {
			double* yi = &(y[i]);
			double* xi = &(x[i]);
			packedx = _mm256_load_pd(xi);
			packedy = _mm256_load_pd(yi);
			packedy = _mm256_mul_pd(packedy, packedx);
			packedy = _mm256_add_pd(packedy, offsets);
			_mm256_store_pd(yi, packedy);
		}
		for (; i < vectorSize; ++i) {
			y[i] *= x[i];
			y[i] += offset;
		}
	}

	(*env)->ReleasePrimitiveArrayCritical(env, jcoef, coef, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, jx, x, 0);
	(*env)->ReleasePrimitiveArrayCritical(env, jy, y, 0);
}

JNIEXPORT void JNICALL
native_jni_avxu(JNIEnv *env, jclass cls, jdoubleArray jcoef, jlong jx,
		jlong jy, jint degree, jint vectorSize) {

	double* coef = (*env)->GetPrimitiveArrayCritical(env, jcoef, 0);
	double* x = (double*)jx;
	double* y = (double*)jy;

	int i;
	for (i = 0; i < vectorSize; ++i)
		y[i] = coef[degree];
	int boundavx = vectorSize - 4;
	for (int d = degree - 1; d > -1; --d) {
		double offset = coef[d];
		__m256d packedy, packedx;
		__m256d offsets = _mm256_broadcast_sd(&offset);
		for (i = 0; i < boundavx; i += 4) {
			double* yi = &(y[i]);
			double* xi = &(x[i]);
			packedx = _mm256_loadu_pd(xi);
			packedy = _mm256_loadu_pd(yi);
			packedy = _mm256_mul_pd(packedy, packedx);
			packedy = _mm256_add_pd(packedy, offsets);
			_mm256_storeu_pd(yi, packedy);
		}
		for (; i < vectorSize; ++i) {
			y[i] *= x[i];
			y[i] += offset;
		}
	}

	(*env)->ReleasePrimitiveArrayCritical(env, jcoef, coef, 0);
}
