package polynomialComputation;

public class Polynome {

	public double[] coef;
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

	public void computePacked(double[] x, double[] y) {

		for (int i = 0; i < x.length; ++i)
			y[i] = compute(x[i]);
	}

}
