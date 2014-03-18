package arrayProd;
import java.lang.reflect.Field;

import sun.misc.Unsafe;
import info.yeppp.Core;

public class arrayProd {

	static final int N;
	static final boolean nanos;
	static final int packetSize;
	static final int nbpacket;
	static final int IT;
	static final Unsafe UNSAFE;
	static final boolean NATIVE;
	static final implementation TYPE;
	static final boolean VERBOSE;

	static {

		String N_S = System.getProperty("args.N");
		N = N_S == null ? 1 << 10 : 1 << Integer.parseInt(N_S);

		String nanos_S = System.getProperty("args.nanos");
		nanos = nanos_S == null ? true : Boolean.parseBoolean(nanos_S);

		String packetSize_S = System.getProperty("args.packet");
		packetSize = packetSize_S == null ? 1 << 10 : 1 << Integer
				.parseInt(packetSize_S);

		String IT_S = System.getProperty("args.IT");
		IT = IT_S == null ? 10000 : Integer.parseInt(IT_S);

		nbpacket = N / packetSize;

		String NATIVE_S = System.getProperty("args.NATIVE");
		NATIVE = NATIVE_S == null ? false : Boolean.parseBoolean(NATIVE_S);

		String TYPE_S = System.getProperty("args.TYPE");
		TYPE = TYPE_S == null ? implementation.PROD_NAIVE : implementation
				.valueOf(TYPE_S);

		String VERBOSE_S = System.getProperty("args.VERBOSE");
		VERBOSE = VERBOSE_S == null ? false : Boolean.parseBoolean(VERBOSE_S);

		long dataSize = 2L * 8L * N;
		System.out.println(humanReadableByteCount(dataSize, true)
				+ " ARITHMETIC INTENSITY " + 1. / 16. + " flop/byte");
		System.loadLibrary("ArrayProd");

		Unsafe a = null;
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			a = (Unsafe) f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UNSAFE = a;
	}

	public enum implementation {
		PROD_NAIVE, 
		PROD_UNROLL4, 
		PROD_UNROLL8, 
		PROD_VECTORIZED, 
		PROD_SAMEOFFSET, 
		
		PROD_UNSAFE_UNROLL4, 
		PROD_UNSAFE_NAIVE, 
		PROD_UNSAFE_SAMEOFFSET, 
		
		PROD2, 
		PROD3, 
		PROD4, 
		
		PROD_JNI_YEPPP, 
		PROD_JNI_NAIVE, 
		PROD_JNI_CPY, 
		PROD_JNI_AVX, 
		
		PROD_NAT_NAIVE, 
		PROD_NAT_UNROLL4, 
		PROD_NAT_UNROLL8, 
		PROD_NAT_JNI_NAIVE, 
		PROD_NAT_JNI_AVX
	}

	public static void main(String[] args) {

		mainNodispatch();
	}

	public static void mainNodispatch() {

		long an = 0;
		long bn = 0;
		double[] a = null;
		double[] b = null;

		if (NATIVE) {

			an = UNSAFE.allocateMemory(N * 8L);
			bn = UNSAFE.allocateMemory(N * 8L);

			for (int i = 0; i < N; ++i) {
				UNSAFE.putDouble(an + (i * 8L), i);
				UNSAFE.putDouble(bn + (i * 8L), N - i);
			}

		} else {
			a = new double[N];
			b = new double[N];
			for (int i = 0; i < N; ++i) {
				a[i] = i;
				b[i] = N - i;
			}
		}

		long t = 0;

		switch (TYPE) {
		case PROD_NAIVE:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prod(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD2:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prod2(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD3:

			t = getTime();
			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prod3(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD4:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prod4(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_UNROLL4:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodUnroll4(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = (i == 0) ? loct : (t > loct ? loct : t);
			}
			break;

		case PROD_UNROLL8:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodUnroll8(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = (i == 0) ? loct : (t > loct ? loct : t);
			}
			break;

		case PROD_UNSAFE_UNROLL4:
			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodUnsafeUnroll4(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_JNI_YEPPP:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					Core.Multiply_IV64fV64f_IV64f(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_UNSAFE_NAIVE:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodUnsafe(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_VECTORIZED:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodVectorizable(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_SAMEOFFSET:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodSameOffset(a, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_UNSAFE_SAMEOFFSET:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodUnsafeSameOffset(a, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_JNI_NAIVE:
			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodJNI(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_JNI_CPY:
			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodJNIcpy(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_JNI_AVX:
			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodJNIAVX(a, p, b, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_NAT_NAIVE:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodn(an, p, bn, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_NAT_UNROLL4:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodnUnroll4(an, p, bn, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_NAT_UNROLL8:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodnUnroll8(an, p, bn, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_NAT_JNI_NAIVE:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodnJNI(an, p, bn, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		case PROD_NAT_JNI_AVX:

			for (int i = 0; i < IT; ++i) {
				long loct = getTime();
				for (int p = 0; p < N; p += packetSize)
					prodnJNIAVXu(an, p, bn, p, packetSize);
				loct = getTime() - loct;
				t = i == 0 ? loct : t > loct ? loct : t;
			}
			break;

		}

		double gflops = ((double) N) / ((double) t);
		System.out.println("N;" + N + ";packetSize;" + packetSize
				+ ";nbpacket;" + nbpacket + ";type;" + TYPE + ";time(nanos);"
				+ t + ";Kflops;" + gflops);

		if (VERBOSE) {

			if (NATIVE) {
				System.out.print("[");
				int i = 0;
				for (i = 0; i < 4; ++i) {
					double ai = UNSAFE.getDouble(an + (i * 8L));
					System.out.print(ai + ", ");
				}
				double ai = UNSAFE.getDouble(an + (i * 8L));
				System.out.println(ai + "]");

			} else {
				System.out.print("[");
				int i = 0;
				for (i = 0; i < 4; ++i) {
					System.out.print(a[i] + ", ");
				}
				System.out.println(a[i] + "]");
			}
		}
	}

	public static void prod(double[] a, int offseta, double[] b, int offsetb,
			int length) {

		for (int i = 0; i < length; ++i) {
			a[offseta + i] *= b[offsetb + i];
		}

	}

	public final static void prod2(final double[] a, final int offseta,
			double[] b, final int offsetb, final int length) {

		final int diff = offsetb - offseta;
		final int bound = offseta + length;
		for (int i = offseta; i < bound; ++i) {
			a[i] *= b[i + diff];
		}

	}

	public final static void prod3(final double[] a, final int offseta,
			double[] b, final int offsetb, final int length) {

		for (int i = 0; i < length; ++i) {
			a[offseta + i] *= b[offsetb + i];
		}

	}

	public final static void prod4(final double[] a, final int offseta,
			double[] b, final int offsetb, final int length) {

		final int diff = offsetb - offseta;
		final int bound = offseta + length;
		for (int i = offseta; i < bound; ++i) {
			double ai = a[i];
			double bi = b[diff + i];
			double prod = ai * bi;
			a[i] = prod;
		}

	}

	public static void prodUnsafe(double[] a, int offseta, double[] b,
			int offsetb, int length) {

		long basea = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (8L * offseta);
		long baseb = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (8L * offsetb);
		long lengthl = 8L * length;

		for (int i = 0; i < lengthl; i += 8L) {

			long aai = basea + i;
			UNSAFE.putDouble(a, aai,
					UNSAFE.getDouble(b, baseb + i) * UNSAFE.getDouble(a, aai));

		}

	}

	public static void prodn(long a, int offseta, long b, int offsetb, int length) {

		long basea = a + (8L * offseta);
		long baseb = b + (8L * offsetb);
		long lengthl = 8L * length;

		for (int i = 0; i < lengthl; i += 8L) {

			long aai = basea + i;
			UNSAFE.putDouble(aai,
					UNSAFE.getDouble(baseb + i) * UNSAFE.getDouble(aai));

		}

	}

	public static void prodUnsafeUnroll4(double[] a, int offseta, double[] b,
			int offsetb, int length) {

		long basea = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (8L * offseta);
		long baseb = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (8L * offsetb);

		long lengthl = 8L * length;
		long bound = lengthl - 32L;

		long i = 0L;
		for (; i < bound; i += 32L) {

			long indexai = basea + i;
			long indexbi = baseb + i;

			double a0 = UNSAFE.getDouble(a, indexai);
			double a1 = UNSAFE.getDouble(a, indexai + 8L);
			double a2 = UNSAFE.getDouble(a, indexai + 16L);
			double a3 = UNSAFE.getDouble(a, indexai + 24L);

			double b0 = UNSAFE.getDouble(b, indexbi);
			double b1 = UNSAFE.getDouble(b, indexbi + 8L);
			double b2 = UNSAFE.getDouble(b, indexbi + 16L);
			double b3 = UNSAFE.getDouble(b, indexbi + 24L);

			double prod0 = a0 * b0;
			double prod1 = a1 * b1;
			double prod2 = a2 * b2;
			double prod3 = a3 * b3;

			UNSAFE.putDouble(a, indexai, prod0);
			UNSAFE.putDouble(a, indexai + 8L, prod1);
			UNSAFE.putDouble(a, indexai + 16L, prod2);
			UNSAFE.putDouble(a, indexai + 24L, prod3);

		}

		for (; i < lengthl; i += 8L) {

			long indexai = basea + i;
			long indexbi = baseb + i;
			double a0 = UNSAFE.getDouble(a, indexai);
			double b0 = UNSAFE.getDouble(b, indexbi);
			double prod0 = a0 + b0;
			UNSAFE.putDouble(b, indexai, prod0);
		}

	}

	public static void prodnUnroll4(long a, int offseta, long b, int offsetb,
			int length) {

		long basea = a + (8L * offseta);
		long baseb = b + (8L * offsetb);

		long lengthl = 8L * length;
		long bound = lengthl - 32L;

		long i = 0L;
		for (; i < bound; i += 32L) {

			long indexai = basea + i;
			long indexbi = baseb + i;

			double a0 = UNSAFE.getDouble(indexai);
			double a1 = UNSAFE.getDouble(indexai + 8L);
			double a2 = UNSAFE.getDouble(indexai + 16L);
			double a3 = UNSAFE.getDouble(indexai + 24L);

			double b0 = UNSAFE.getDouble(indexbi);
			double b1 = UNSAFE.getDouble(indexbi + 8L);
			double b2 = UNSAFE.getDouble(indexbi + 16L);
			double b3 = UNSAFE.getDouble(indexbi + 24L);

			double prod0 = a0 * b0;
			double prod1 = a1 * b1;
			double prod2 = a2 * b2;
			double prod3 = a3 * b3;

			UNSAFE.putDouble(indexai, prod0);
			UNSAFE.putDouble(indexai + 8L, prod1);
			UNSAFE.putDouble(indexai + 16L, prod2);
			UNSAFE.putDouble(indexai + 24L, prod3);

		}

		for (; i < lengthl; i += 8L) {

			long indexai = basea + i;
			long indexbi = baseb + i;
			double a0 = UNSAFE.getDouble(indexai);
			double b0 = UNSAFE.getDouble(indexbi);
			double prod0 = a0 + b0;
			UNSAFE.putDouble(indexai, prod0);
		}

	}

	public static void prodnUnroll8(long a, int offseta, long b, int offsetb,
			int length) {

		long basea = a + (8L * offseta);
		long baseb = b + (8L * offsetb);

		long lengthl = 8L * length;
		long bound = lengthl - 64L;

		long i = 0L;
		for (; i < bound; i += 64L) {

			long indexai = basea + i;
			long indexbi = baseb + i;

			double a0 = UNSAFE.getDouble(indexai);
			double a1 = UNSAFE.getDouble(indexai + 8L);
			double a2 = UNSAFE.getDouble(indexai + 16L);
			double a3 = UNSAFE.getDouble(indexai + 24L);
			double a4 = UNSAFE.getDouble(indexai + 32L);
			double a5 = UNSAFE.getDouble(indexai + 40L);
			double a6 = UNSAFE.getDouble(indexai + 48L);
			double a7 = UNSAFE.getDouble(indexai + 56L);

			double b0 = UNSAFE.getDouble(indexbi);
			double b1 = UNSAFE.getDouble(indexbi + 8L);
			double b2 = UNSAFE.getDouble(indexbi + 16L);
			double b3 = UNSAFE.getDouble(indexbi + 24L);
			double b4 = UNSAFE.getDouble(indexbi + 32L);
			double b5 = UNSAFE.getDouble(indexbi + 40L);
			double b6 = UNSAFE.getDouble(indexbi + 48L);
			double b7 = UNSAFE.getDouble(indexbi + 56L);

			double prod0 = a0 * b0;
			double prod1 = a1 * b1;
			double prod2 = a2 * b2;
			double prod3 = a3 * b3;
			double prod4 = a4 * b4;
			double prod5 = a5 * b5;
			double prod6 = a6 * b6;
			double prod7 = a7 * b7;

			UNSAFE.putDouble(indexai, prod0);
			UNSAFE.putDouble(indexai + 8L, prod1);
			UNSAFE.putDouble(indexai + 16L, prod2);
			UNSAFE.putDouble(indexai + 24L, prod3);
			UNSAFE.putDouble(indexai + 32L, prod4);
			UNSAFE.putDouble(indexai + 40L, prod5);
			UNSAFE.putDouble(indexai + 48L, prod6);
			UNSAFE.putDouble(indexai + 56L, prod7);

		}

		for (; i < lengthl; i += 8L) {

			long indexai = basea + i;
			long indexbi = baseb + i;
			double a0 = UNSAFE.getDouble(indexai);
			double b0 = UNSAFE.getDouble(indexbi);
			double prod0 = a0 + b0;
			UNSAFE.putDouble(indexai, prod0);
		}

	}

	public static void prodUnroll4(double[] a, int offseta, double[] b,
			int offsetb, int length) {

		int i = 0;
		int bound = length - 4;
		for (; i < bound; i += 4) {
			int ia = offseta + i;
			int ib = offsetb + i;
			a[ia] *= b[ib];
			a[ia + 1] *= b[ib + 1];
			a[ia + 2] *= b[ib + 2];
			a[ia + 3] *= b[ib + 3];
		}
		for (; i < length; ++i)
			a[offseta + i] *= b[offsetb + i];

	}

	public static void prodUnroll8(double[] a, int offseta, double[] b,
			int offsetb, int length) {

		int i = 0;
		int bound = length - 8;
		for (; i < bound; i += 8) {
			int ia = offseta + i;
			int ib = offsetb + i;
			a[ia] *= b[ib];
			a[ia + 1] *= b[ib + 1];
			a[ia + 2] *= b[ib + 2];
			a[ia + 3] *= b[ib + 3];
			a[ia + 4] *= b[ib + 4];
			a[ia + 5] *= b[ib + 5];
			a[ia + 6] *= b[ib + 6];
			a[ia + 7] *= b[ib + 7];
		}

		for (; i < length; ++i)
			a[offseta + i] *= b[offsetb + i];

	}

	public static void prodVectorizable(double[] a, int offseta, double[] b,
			int offsetb, int length) {

		if (offseta == offsetb)
			prodSameOffset(a, b, offseta, length);
		else
			prod(a, offseta, b, offsetb, length);

	}

	public static void prodFullVectorized(double[] a, double[] b) {

		for (int i = 0; i < a.length; ++i)
			a[i] *= b[i];

	}

	public static void prodSameOffset(double[] a, double[] b, int beg, int length) {

		int bound = beg + length;
		for (int i = beg; i < bound; ++i)
			a[i] *= b[i];

	}

	public static void prodUnsafeSameOffset(double[] a, double[] b, int beg,
			int length) {

		long base = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (8L * beg);
		long bound = base + (8L * length);

		for (long i = base; i < bound; i += 8L) {
			double ai = UNSAFE.getDouble(a, i);
			double bi = UNSAFE.getDouble(b, i);
			double prod = ai * bi;
			UNSAFE.putDouble(a, i, prod);
		}

	}

	public static void prodFullVectorized2(double[] a, double[] b) {

		for (int i = 0; i < a.length; ++i) {
			double ai = a[i];
			double bi = b[i];
			double prod = ai + bi;
			a[i] = prod;
		}
	}
	
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		double d = bytes / Math.pow(unit, exp);
		return String.format("%.1f %sB", new Double(d), pre);
	}

	private static long getTime() {
		if (nanos)
			return System.nanoTime();
		return System.currentTimeMillis();
	}

	public static native void prodJNIcpy(double[] a, int offseta, double[] b,
			int offsetb, int lenght);

	public static native void prodJNIAVX(double[] a, int offseta, double[] b,
			int offsetb, int lenght);

	public static native void prodJNI(double[] a, int offseta, double[] b,
			int offsetb, int lenght);

	public static native void prodnJNI(long a, int offseta, long b, int offsetb,
			int lenght);

	public static native void prodnJNIAVXu(long a, int offseta, long b,
			int offsetb, int lenght);

	// public static native void prodJNIconstMem(long jni_memory, double[] a, int
	// offseta, double[] b,
	// int offsetb, int lenght);

}
