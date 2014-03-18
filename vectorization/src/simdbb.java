import info.yeppp.Core;

public class simdbb {

	public static void main(String[] args) {

		double[] a = new double[4];
		for (int i = 0; i < 10_000_000; ++i) {
			add4(a, a);
			Core.Add_IV64fV64f_IV64f(a, 0, a, 0, 4);
		}
	}

	public static void add4(double[] a, double[] b) {

		a[0] += b[0];
		a[1] += b[1];
		a[2] += b[2];
		a[3] += b[3];

	}

}
