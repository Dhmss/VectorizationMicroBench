package polynomialComputation;

import java.lang.reflect.Field;
import java.util.Arrays;

import sun.misc.Unsafe;

// HORNER

// flop : 3ND flop

// byte : 8(D + 2N + 1)

// arithmetic intensity : 3ND / 8(D+2N +1)

// CSTE AI and CSTE size S : D = S - 2N - 1 and 6N² - 3N(S-1) + 8AS = 0

// CSTE AI and CSTE flop F (F8 = F/8A): D = F8 - 2N - 1 and 6N² - 3N(F8-1) +

public class polyEval {

	public static final int N;
	public static final int D;
	public static final int IT;
	public static final int WU;
	public static final double AI;
	public static final long FLOP;
	public static final long DataSize;
	public static final int ALIGNMENT;
	public static final boolean TESTVALUE;
	public static final Unsafe UNSAFE;

	static {

		System.loadLibrary("PolyEval");

		registerNatives();

		Unsafe a = null;
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			a = (Unsafe) f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UNSAFE = a;

		String A_S = System.getProperty("args.alignment");
		ALIGNMENT = A_S == null ? 0 : Integer.parseInt(A_S);

		String T_S = System.getProperty("args.testvalue");
		TESTVALUE = T_S == null ? false : true;

		String N_S = System.getProperty("args.Nb2");
		N = N_S == null ? 1 << 10 : 1 << Integer.parseInt(N_S);

		String D_S = System.getProperty("args.D");
		D = D_S == null ? 64 : Integer.parseInt(D_S);

		String D_IT = System.getProperty("args.IT");
		IT = D_IT == null ? 1000 : Integer.parseInt(D_IT);

		String D_WU = System.getProperty("args.WU");
		WU = D_WU == null ? 10000 : Integer.parseInt(D_WU);

		DataSize = 8 * (D + (2 * N) + 1);

		FLOP = 3 * N * D;

		AI = ((double) FLOP) / ((double) DataSize);

		System.out.println("\nDATA SIZE "
				+ humanReadableByteCount(DataSize, true));
		System.out.println("CPLXITY " + FLOP + " flop");
		System.out.println("ARITHMETIC INTENSITY " + AI + " flop/byte");
		System.out.println("ALIGNMENT " + ALIGNMENT + " x8 bytes");
		System.out.println("WU:" + WU + " IT:" + IT + "\n");

	}

	public static void main(String[] args) {

		if (TESTVALUE) {
			testValue();
			return;
		}

		double[] coef = new double[D + 1];
		final int oversize = N + ALIGNMENT;
		double[] x = new double[oversize];
		double[] y = new double[oversize];

		long nx = UNSAFE.allocateMemory(8L * oversize);
		long ny = UNSAFE.allocateMemory(8L * oversize);

		// touch
		for (int i = 0; i < oversize; ++i) {
			long offset = 8L * i;
			long ad = nx + offset;
			UNSAFE.putDouble(ad, 0);
			ad = ny + offset;
			UNSAFE.putDouble(ad, 0);
		}

		go_yeppp(coef, x, y);
		go_basic(coef, x, y);
		go_basic_aligned(coef, x, y);
		go_basic_unaligned(coef, x, y);
		go_jni_avxu(coef, x, y);
		go_jni_avxa(coef, x, y);
		go_jni_basic(coef, x, y);
		go_native_jni_avxu(coef, nx, ny);

	}

	public static void go_basic(double[] coef, double[] x, double[] y) {
		System.out.println("\nBASIC");

		for (int i = 0; i < WU; ++i)
			basic(coef, x, y, ALIGNMENT, N);

		System.out.println("END WARMUP");

		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			basic(coef, x, y, ALIGNMENT, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_basic_aligned(double[] coef, double[] x, double[] y) {
		System.out.println("\nBASIC_ALIGNED");

		for (int i = 0; i < WU; ++i)
			basic_aligned(coef, x, y, N);

		System.out.println("END WARMUP");

		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			basic_aligned(coef, x, y, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_basic_unaligned(double[] coef, double[] x, double[] y) {
		System.out.println("\nBASIC_UNALIGNED");

		for (int i = 0; i < WU; ++i)
			basic_unaligned(coef, x, y, N);

		System.out.println("END WARMUP");

		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			basic_unaligned(coef, x, y, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_yeppp(double[] coef, double[] x, double[] y) {

		System.out.println("\nYEPPP");

		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			info.yeppp.Math.EvaluatePolynomial_V64fV64f_V64f(coef, 0, x,
					ALIGNMENT, y, ALIGNMENT, D + 1, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_jni_avxu(double[] coef, double[] x, double[] y) {

		System.out.println("\nJNI_AVX_UNALIGNED");
		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			jni_avxu(coef, x, y, D, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_jni_basic(double[] coef, double[] x, double[] y) {

		System.out.println("\nJNI_BASIC");
		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			jni_basic(coef, x, y, D, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_jni_avxa(double[] coef, double[] x, double[] y) {

		System.out.println("\nJNI_AVX_ALIGNED");

		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			jni_avxa(coef, x, y, D, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	public static void go_native_jni_avxu(double[] coef, long x, long y) {

		System.out.println("\nNATIVE_JNI_AVX_UNALIGNED");
		long t = Long.MAX_VALUE;
		for (int i = 0; i < IT; ++i) {
			long loct = System.nanoTime();
			native_jni_avxu(coef, x, y, D, N);
			loct = System.nanoTime() - loct;
			t = loct < t ? loct : t;
		}

		printResult(t);
	}

	private static void basic_enc(double[] coef, double[] x, double[] y,
			int offset, int vectorSize) {

		int deg = coef.length - 1;

		for (int i = offset; i < vectorSize; ++i)
			y[i] = coef[deg];

		for (int d = deg - 1; d > -1; --d)
			triad(coef[d], x, y, offset, vectorSize);

	}

	private static void triad(double coef, double[] x, double[] y, int offset,
			int vectorSize) {

		for (int i = offset; i < x.length; ++i) {
			y[i] *= x[i];
			y[i] += coef;
		}

	}

	private static void basic(double[] coef, double[] x, double[] y,
			int offset, int vectorSize) {

		final int deg = coef.length - 1;
		final int bound = offset + vectorSize;

		for (int i = offset; i < bound; ++i)
			y[i] = coef[deg];

		for (int d = deg - 1; d > -1; --d) {

			double coefd = coef[d];
			for (int i = offset; i < bound; ++i) {
				y[i] *= x[i];
				y[i] += coefd;
			}

		}

	}

	private static void basic_aligned(double[] coef, double[] x, double[] y,
			int vectorSize) {

		final int deg = coef.length - 1;
		final int bound = ALIGNMENT + vectorSize;

		// Y = coef[d]
		for (int i = ALIGNMENT; i < bound; ++i)
			y[i] = coef[deg];

		for (int d = deg - 1; d > -1; --d) {

			double coefd = coef[d];
			for (int i = ALIGNMENT; i < bound; ++i) {
				y[i] *= x[i];
				y[i] += coefd;
			}

		}

	}

	private static void basic_unaligned(double[] coef, double[] x, double[] y,
			int vectorSize) {

		final int deg = coef.length - 1;

		// Y = coef[d]
		for (int i = 0; i < vectorSize; ++i)
			y[i] = coef[deg];

		for (int d = deg - 1; d > -1; --d) {

			double coefd = coef[d];
			for (int i = 0; i < vectorSize; ++i) {
				y[i] *= x[i];
				y[i] += coefd;
			}

		}

	}

	private static void printResult(long dt) {

		System.out.println("ELAPSED " + dt + " nanos");
		double gflops = FLOP / (double) dt;
		System.out.println("PERFORMANCE " + gflops + " GFlops");
		double bandwidth = gflops / AI;
		System.out.println("BANDWIDTH " + bandwidth + " GB/s");

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

	public static void testValue() {

		double[] coef = { 0, 1, 0 };
		double[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		double[] y = new double[10];

		System.out.println("EXPECTED\n" + Arrays.toString(x));
		jni_basic(coef, x, y, 2, 10);
		System.out.println("RETURN\n" + Arrays.toString(y));

	}

	private static native void jni_avxu(double[] coef, double[] x, double[] y,
			int degree, int vectorSize);

	private static native void jni_avxa(double[] coef, double[] x, double[] y,
			int degree, int vectorSize);

	private static native void jni_basic(double[] coef, double[] x, double[] y,
			int degree, int vectorSize);

	private static native void native_jni_avxu(double[] coef, long x, long y,
			int degree, int vectorSize);

	private static native void registerNatives();

}
