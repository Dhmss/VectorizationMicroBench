public class sum {

	static int wu = 50000;
	static int it = 1000000;

	final double[] a;

	public static void main(String[] args) {
		//main0();
		main1();
	}

	public static void main0() {
		long[] t = new long[3];
		sum b = new sum();

		double sum = 0;
		for (int i = 0; i < wu; ++i)
			sum += b.sum0();

		System.out.println("\n warmup \n");

		t[0] = System.currentTimeMillis();
		for (int i = 0; i < it; ++i)
			sum += b.sum0();
		t[0] = System.currentTimeMillis() - t[0];

		System.out.println(sum);

		// ********************************
		sum = 0;
		for (int i = 0; i < wu; ++i)
			sum += b.sum1();

		System.out.println("\n warmup \n");

		t[1] = System.currentTimeMillis();
		for (int i = 0; i < it; ++i)
			sum += b.sum1();
		t[1] = System.currentTimeMillis() - t[1];

		System.out.println(sum);

		// ********************************
		sum = 0;
		for (int i = 0; i < wu; ++i)
			sum += b.sum2();

		System.out.println("\n warmup \n");

		t[2] = System.currentTimeMillis();
		for (int i = 0; i < it; ++i)
			sum += b.sum2();
		t[2] = System.currentTimeMillis() - t[2];

		System.out.println(sum);

		System.out.println("sum0 " + t[0]);
		System.out.println("sum1 " + t[1]);
		System.out.println("sum2 " + t[2]);
	}

	public static void main1() {
		long[] t = new long[3];
		double[] a = new double[2048];

		double sum = 0;
		for (int i = 0; i < wu; ++i)
			sum += sum0(a);

		System.out.println("\n warmup \n");

		t[0] = System.currentTimeMillis();
		for (int i = 0; i < it; ++i)
			sum += sum0(a);
		t[0] = System.currentTimeMillis() - t[0];

		System.out.println(sum);

		// ********************************
		sum = 0;
		for (int i = 0; i < wu; ++i)
			sum += sum1(a);

		System.out.println("\n warmup \n");

		t[1] = System.currentTimeMillis();
		for (int i = 0; i < it; ++i)
			sum += sum1(a);
		t[1] = System.currentTimeMillis() - t[1];

		System.out.println(sum);

		// ********************************
		sum = 0;
		for (int i = 0; i < wu; ++i)
			sum += sum2(a);

		System.out.println("\n warmup \n");

		t[2] = System.currentTimeMillis();
		for (int i = 0; i < it; ++i)
			sum += sum2(a);
		t[2] = System.currentTimeMillis() - t[2];

		System.out.println(sum);

		System.out.println("sum0 " + t[0]);
		System.out.println("sum1 " + t[1]);
		System.out.println("sum2 " + t[2]);
	}

	public sum() {

		a = new double[2048];

	}

	public double sum0() {

		double sum = 0;
		for (int i = 0; i < a.length; ++i)
			sum += a[i];

		return sum;
	}

	public static double sum0(double[] a) {

		double sum = 0;
		for (int i = 0; i < a.length; ++i)
			sum += a[i];

		return sum;

	}

	public double sum1() {

		double sum = 0;
		for (int i = 0; i < a.length; i += 4)
			sum += a[i] + a[i + 1] + a[i + 2] + a[i + 3];

		return sum;

	}

	public static double sum1(double[] a) {

		double sum = 0;
		for (int i = 0; i < a.length; i += 4)
			sum += a[i] + a[i + 1] + a[i + 2] + a[i + 3];

		return sum;

	}

	public double sum2() {

		double sum = 0;
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for (int i = 0; i < a.length; i += 4) {
			sum += a[i];
			sum1 += a[i + 1];
			sum2 += a[i + 2];
			sum3 += a[i + 3];
		}

		return sum + sum1 + sum2 + sum3;

	}

	public static double sum2(double[] a) {

		double sum = 0;
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		for (int i = 0; i < a.length; i += 4) {
			sum += a[i];
			sum1 += a[i + 1];
			sum2 += a[i + 2];
			sum3 += a[i + 3];
		}

		return sum + sum1 + sum2 + sum3;

	}

}
