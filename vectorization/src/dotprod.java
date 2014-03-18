import info.yeppp.Core;

import java.util.Random;

public class dotprod {

	public static int N = Integer.parseInt(System
			.getProperty("args.N", "32768"));
	private static int length = Integer.parseInt(System.getProperty(
			"args.length", "8192"));

	public static int IT = Integer
			.parseInt(System.getProperty("args.IT", "100"));

	public static void main(String[] args) {

		int ncall = IT * (N / length);
		int n = ncall * length;
		System.out.println("iteration = " + IT);
		System.out.println("total size = " + n);
		System.out.println("flop = " + (2 * n));
		System.out.println("packed size = " + length);
		System.out.println("nb call = " + ncall);

		String log = IT + ";" + (2 * n) + ";" + ncall + ";";

		double[] b = new double[n];
		Random r = new Random(0);
		long t;
		double result;
		for (int i = 0; i < n; ++i)
			b[i] = r.nextDouble();
		System.out.println("\n END INITIALIZATION \n");

		// ****************************************
		result = 0;
		double[] x = new double[] { 1, 1, 1, 1 };
		for (int i = 0; i < 10000; ++i)
			Core.DotProduct_V64fV64f_S64f(x, 0, x, 0, 4);

		System.out.println("END WARMUP");

		t = System.currentTimeMillis();
		for (int it = 0; it < IT; ++it)
			for (int i = 0; i < n; i += length) {
				result += Core.DotProduct_V64fV64f_S64f(b, i, b, i, length);
			}

		t = System.currentTimeMillis() - t;
		System.out.println("NATIVE\nRESULT       : " + result
				+ "\nELAPSED TIME : " + t + " ms");

		log += t + ";";
		// ****************************************

		result = 0;
		for (int i = 0; i < 10000; ++i)
			dotproduct(x, x, 0, 4);

		System.out.println("END WARMUP");

		t = System.currentTimeMillis();
		for (int it = 0; it < IT; ++it)
			for (int i = 0; i < n; i += length) {
				result += dotproduct(b, b, i, length);
			}

		t = System.currentTimeMillis() - t;
		System.out.println("NORMAL\nRESULT       : " + result
				+ "\nELAPSED TIME : " + t + " ms");

		log += t + ";";
		// ****************************************
		result = 0;
		for (int i = 0; i < 10000; ++i)
			dotproductU0(x, x, 0, 4);

		System.out.println("END WARMUP");

		t = System.currentTimeMillis();
		for (int it = 0; it < IT; ++it)
			for (int i = 0; i < n; i += length) {
				result += dotproductU0(b, b, i, length);
			}

		t = System.currentTimeMillis() - t;
		System.out.println("UNROLL0\nRESULT       : " + result
				+ "\nELAPSED TIME : " + t + " ms");

		log += t + ";";
		// ****************************************
		result = 0;
		for (int i = 0; i < 10000; ++i)
			dotproductU1(x, x, 0, 4);

		System.out.println("END WARMUP");

		t = System.currentTimeMillis();
		for (int it = 0; it < IT; ++it)
			for (int i = 0; i < n; i += length) {
				result += dotproductU1(b, b, i, length);
			}

		t = System.currentTimeMillis() - t;
		System.out.println("UNROLL1\nRESULT       : " + result
				+ "\nELAPSED TIME : " + t + " ms");
		
		log += t + ";";
		// ****************************************

		System.out.println(log);

	}

	public static boolean compute(boolean[] b) {
		boolean res = true;

		for (int i = 0; i < b.length - 4; ++i)
			res &= intersection1(b[i], b[i + 1], b[i + 2], b[i + 3]);

		return res;
	}

	public static boolean intersection1(boolean b0, boolean b1, boolean b2,
			boolean b3) {

		return b0 & b1 & b2 & b3;

	}

	public static boolean intersection2(boolean b0, boolean b1, boolean b2,
			boolean b3) {

		return b0 && b1 && b2 && b3;

	}

	public static double dotproduct(double[] a, double[] b, int offset,
			int length) {

		double result = 0;
		int bound = offset + length;
		for (int i = offset; i < bound; ++i) {
			result += a[i] * b[i];
		}
		return result;
	}

	public static double dotproductU0(double[] a, double[] b, int offset,
			int length) {

		double result = 0;
		int i = offset;
		int bound = offset + length;
		for (; i < bound; i += 4) {
			result += a[i] * b[i];
			result += a[i + 1] * b[i + 1];
			result += a[i + 2] * b[i + 2];
			result += a[i + 3] * b[i + 3];
		}

		for (; i < bound; ++i)
			result += a[i] * b[i];

		return result;
	}

	public static double dotproductU1(double[] a, double[] b, int offset,
			int length) {

		double result = 0;
		double result1 = 0;
		double result2 = 0;
		double result3 = 0;

		int bound = offset + length;
		int i = offset;
		for (; i < bound; i += 4) {
			result += a[i] * b[i];
			result1 += a[i + 1] * b[i + 1];
			result2 += a[i + 2] * b[i + 2];
			result3 += a[i + 3] * b[i + 3];
		}

		for (; i < bound; ++i)
			result += a[i] * b[i];

		return result + result1 + result2 + result3;
	}

	public static double dotproductN(double[] a, double[] b, int offset,
			int lenght) {

		return Core.DotProduct_V64fV64f_S64f(a, offset, b, offset, lenght);
	}

}
