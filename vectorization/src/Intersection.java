public class Intersection {

	static double X0, X1, Y0, Y1;

	public static boolean int0(double x0, double y0, double x1, double y1) {
		return (x1 > X0) && (x0 < X1) && (y1 > Y0) && (y0 < Y1); // // true if
																	// intersect
	}

	public static boolean int1(double x0, double y0, double x1, double y1) {
		return (x1 > X0) & (x0 < X1) & (y1 > Y0) & (y0 < Y1); // true if
																// intersect
	}

	public static boolean quadInclusion(double x0, double y0, double x1,
			double y1) {

		double mx = (X0 + X1) * 0.5;
		double my = (Y0 + Y1) * 0.5;

		return !(((x0 < mx) ^ (x1 < mx)) | ((y0 < my) ^ (y1 < my)));

	}
}
