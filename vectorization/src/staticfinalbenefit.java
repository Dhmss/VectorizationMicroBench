public class staticfinalbenefit {

	public static final int N = 8;
	
	public static void main(String[] args) {
		
		double[] a = new double[8192];
		
		long t = System.currentTimeMillis();
		
		for(int i = 0 ;  i < 1000000 ; ++i)
			sum2(a);
		
		t = System.currentTimeMillis() - t;

		System.out.println(t);
		
		t = System.currentTimeMillis();
		
		for(int i = 0 ;  i < 1000000 ; ++i)
			sum(a);
		
		t = System.currentTimeMillis() - t;

		System.out.println(t);
		
	}
	public static double sum(double[] a) {

		double sum = 0;

		for (int i = 0; i < a.length; ++i)
			sum += a[i];

		return sum;
	}
	
	public static double sum2(double[] a) {

		double sum = 0;

		for (int i = 0; i < N; ++i)
			sum += a[i];

		return sum;
	}

}
