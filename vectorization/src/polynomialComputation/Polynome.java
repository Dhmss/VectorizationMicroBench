package polynomialComputation;

public class Polynome {

	public final double[] coef;
	public int deg;

	public Polynome(double[] coef) {
		this.coef = coef;
		deg = coef.length - 1;
	}

	public double compute(double x) {

		double y = coef[deg];

		for (int d = deg - 1; d > -1; --d) {
			y *= x;
			y += coef[d];
		}

		return y;
	}

	public void computePackedu(double[] x, double[] y, int n) {
		for (int i = 0; i < n; ++i)
			y[i] = compute(x[i]);
	}

}
