package crossProdSign2d;

import java.math.BigInteger;
import java.util.Random;

public class CrossProdSign {

	private static final int N;

	private static final int IT;

	public static final long[] val;

	public static long signature;

	static {

		String Ns = System.getProperty("args.N");

		N = Ns == null ? 16048 : Integer.parseInt(Ns);

		val = new long[4 * N];

		String ITs = System.getProperty("args.IT");

		IT = ITs == null ? 2000 : Integer.parseInt(ITs);
		// System.loadLibrary("crossProductSign2");

	}

	public static int ofi = 0;
	public static int ofd = 0;
	public static int sd = 0;
	public static int nofi = 0;
	public static int md = 0;

	public static void main(String[] args) {

		setUp();
		signature = 0;
		long dt = Long.MAX_VALUE;
		for (int it = 0; it < IT; ++it) {
			long tloc = System.nanoTime();
			for (int i = 0; i < val.length; i += 4) {
				long l0 = val[i];
				long l1 = val[i + 1];
				long r0 = val[i + 2];
				long r1 = val[i + 3];
				signature += crossProdSign4(l0, l1, r0, r1);
			}
			tloc = System.nanoTime() - tloc;
			dt = tloc < dt ? tloc : dt;
		}

		System.out.println("VERSION 0 " + signature);
		System.out.println("SIGNATURE " + signature);
		System.out.println("TIME      " + dt + " nanos");

		signature = 0;
		dt = Long.MAX_VALUE;
		for (int it = 0; it < IT; ++it) {
			long tloc = System.nanoTime();
			for (int i = 0; i < val.length; i += 4) {
				long l0 = val[i];
				long l1 = val[i + 1];
				long r0 = val[i + 2];
				long r1 = val[i + 3];
				signature += crossProdSign5(l0, l1, r0, r1);

			}
			tloc = System.nanoTime() - tloc;
			dt = tloc < dt ? tloc : dt;
		}

		System.out.println("\nVERSION 5 " + signature);
		System.out.println("SIGNATURE " + signature);
		System.out.println("TIME      " + dt + " nanos");

		signature = 0;
		dt = Long.MAX_VALUE;
		for (int it = 0; it < IT; ++it) {
			long tloc = System.nanoTime();
			for (int i = 0; i < val.length; i += 4) {
				long l0 = val[i];
				long l1 = val[i + 1];
				long r0 = val[i + 2];
				long r1 = val[i + 3];
				signature += crossProdSignLong(l0, l1, r0, r1);
			}
			tloc = System.nanoTime() - tloc;
			dt = tloc < dt ? tloc : dt;
		}

		System.out.println("\nVERSION Long " + signature);
		System.out.println("SIGNATURE " + signature);
		System.out.println("TIME      " + dt + " nanos");
		
		signature = 0;
		dt = Long.MAX_VALUE;
		for (int it = 0; it < IT; ++it) {
			long tloc = System.nanoTime();
			for (int i = 0; i < val.length; i += 4) {
				long l0 = val[i];
				long l1 = val[i + 1];
				long r0 = val[i + 2];
				long r1 = val[i + 3];
				signature += crossProdSignBI(l0, l1, r0, r1);
			}
			tloc = System.nanoTime() - tloc;
			dt = tloc < dt ? tloc : dt;
		}

		System.out.println("\nVERSION BI " + signature);
		System.out.println("SIGNATURE " + signature);
		System.out.println("TIME      " + dt + " nanos");

		double ratio = 100.0 / N;
		System.out.println("null value                " + md + "    " + md
				* ratio + "%\ndetermined sign           " + sd + "    " + sd
				* ratio + "%\noverflow determined       " + ofd + "    " + ofd
				* ratio + "%\nnon-overflow undetermined " + nofi + "    "
				+ nofi * ratio + "%\noverflow undetermined     " + ofi + "    "
				+ ofi * ratio + "%");

	}

	public static void setUp() {

		String rep = System.getProperty("args.repartition", "seed");

		if (rep.equals("seed")) {

			String seeds = System.getProperty("args.seed");

			Random R = new Random(seeds == null ? 16723
					: Integer.parseInt(seeds));

			int offset = 0;
			while (offset < val.length) {

				long l0 = R.nextLong();
				long l1 = R.nextLong();
				long r0 = R.nextLong();
				long r1 = R.nextLong();

				val[offset++] = l0;
				val[offset++] = l1;
				val[offset++] = r0;
				val[offset++] = r1;

				calcRepartition(l0, l1, r0, r1);

			}

		} else {

			String ps = String.valueOf(rep.charAt(0));
			char c = rep.charAt(1);
			int i = 1;
			while (c != '-') {
				ps += String.valueOf(c);
				++i;
				c = rep.charAt(i);
			}
			double P0 = Double.parseDouble(ps);

			++i;
			c = rep.charAt(i);
			ps = String.valueOf(c);
			++i;
			c = rep.charAt(i);
			while (c != '-') {
				ps += String.valueOf(c);
				++i;
				c = rep.charAt(i);
			}
			double Ppos = Double.parseDouble(ps);
			++i;
			c = rep.charAt(i);
			ps = String.valueOf(c);
			++i;
			while (i < rep.length()) {
				ps += String.valueOf(rep.charAt(i));
				++i;
			}
			double Pof = Double.parseDouble(ps);

			setRepartition(0.01 * P0, 0.01 * Ppos, 0.01 * Pof);
		}
	}

	public void tearDownTrial() {

		System.out.println("SIGNATURE " + signature);

		double ratio = 100.0 / N;
		System.out.println("multiplication           " + md + "    " + md
				* ratio + "%");
		System.out.println("signe determine          " + sd + "    " + sd
				* ratio + "%");
		System.out.println("overflow determine       " + ofd + "    " + ofd
				* ratio + "%");
		System.out.println("non-overflow indetermine " + nofi + "    " + nofi
				* ratio + "%");
		System.out.println("overflow indetermine     " + ofi + "    " + ofi
				* ratio + "%");

	}

	private static void calcRepartition(long l0, long l1, long r0, long r1) {

		if (l0 > 0) {
			if (l1 > 0) {
				if (r0 > 0 && r1 > 0) {
					calcRepSignEqualpp(l0, l1, r0, r1);
					return;
				}
				if (r0 < 0 && r1 < 0) {
					calcRepSignEqualpp(l0, l1, -r0, -r1);
					++sd;
					return;
				}

			}
			if (l1 < 0) {
				if (r0 > 0 && r1 < 0) {
					calcRepSignEqualpp(l0, -l1, r0, -r1);
					return;
				}
				if (r0 < 0 && r1 > 0) {
					calcRepSignEqualpp(l0, -l1, -r0, r1);
					return;
				}
				++sd;
				return;
			}
			++md;
			return;
		}
		if (l0 < 0) {
			if (l1 > 0) {
				if (r0 > 0 && r1 < 0) {
					calcRepSignEqualpp(-l0, l1, r0, -r1);
					return;
				}
				if (r0 < 0 && r1 > 0) {
					calcRepSignEqualpp(-l0, l1, -r0, r1);
					return;
				}
				++sd;
				return;
			}
			if (l1 < 0) {
				if (r0 > 0 && r1 > 0) {
					calcRepSignEqualpp(-l0, -l1, r0, r1);
					return;
				}
				if (r0 < 0 && r1 < 0) {
					calcRepSignEqualpp(-l0, -l1, -r0, -r1);
					return;
				}
				++sd;
				return;
			}
			++md;
			return;
		}
		++md;
	}

	private static void calcRepSignEqualpp(long l0, long l1, long r0, long r1) {

		if (isMultOverflow(l0, l1)) {
			if (isMultOverflow(r0, r1)) {
				++ofi;
				return;
			}
			++ofd;
			return;
		}

		if (isMultOverflow(r0, r1)) {
			++ofd;
			return;
		}
		++nofi;
	}

	private static void setRepartition(double P0, double Ppos, double Pof) {

		// Pof en sachant qu'il n'y a aucun terme nuls
		// Ppos en sachant qu'il n'y a pas de terme nuls

		double Pn0 = 1 - P0;
		double Pnm = Pn0 * Pn0 * Pn0 * Pn0; // proba branchement avec l et r non
											// nuls
		double Pm = 1 - Pnm; // proba 1 terme nul

		double PposM = Ppos * Ppos + (1 - Ppos) * (1 - Ppos);
		double PnegM = 1 - PposM;

		double Peq = ((PposM * PposM) + (PnegM * PnegM)) * Pnm; // proba l et r
																// meme signe et
																// non nuls

		double Pneq = Pnm - Peq;
		double Pno = 1 - Pof;
		double Peq0 = Pno * Pno * Peq;
		double Peq2 = Pof * Pof * Peq;
		double Peq1 = Peq - Peq0 - Peq2;

		md = (int) (N * Pm);
		sd = (int) (N * Pneq);
		nofi = (int) (N * Peq0);
		ofd = (int) (N * Peq1);
		ofi = (int) (N * Peq2);

		int NN = md + sd + nofi + ofd + ofi;

		System.out.println(md + " " + sd + " " + nofi + " " + ofd + " " + ofi);

		System.out.println(NN);

		System.out.println(val.length);

		int offset = 0;
		int bound = 4 * md;
		while (offset < bound) {

			val[offset++] = 0;
			val[offset++] = 1;
			val[offset++] = 3;
			val[offset++] = 4;

			offset += 4;

		}

		bound += 4 * sd;
		while (offset < bound) {

			val[offset] = -123;
			val[offset + 1] = 127;
			val[offset + 2] = 128;
			val[offset + 3] = 93;

			offset += 4;

		}

		bound += 4 * nofi;
		while (offset < bound) {

			val[offset] = 23;
			val[offset + 1] = 12;
			val[offset + 2] = 128;
			val[offset + 3] = 938;

			offset += 4;

		}

		bound += 4 * ofd;
		while (offset < bound) {

			val[offset] = Long.MAX_VALUE;
			val[offset + 1] = 2;
			val[offset + 2] = 128;
			val[offset + 3] = 938;

			offset += 4;

		}

		while (offset < val.length) {

			val[offset] = Long.MAX_VALUE;
			val[offset + 1] = 100;
			val[offset + 2] = Long.MAX_VALUE;
			val[offset + 3] = 4;

			offset += 4;

		}

	}

	// signed
	// absolute form
	// check exponent domination
	// check overflow risk
	// bigInteger
	public static int crossProdSign0(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int s = sr0 * sr1 + sl;

		// fail fast, if sign is determined
		if (s == 0)
			return sl;

		// fail fast, if sign is determined
		if (s == 1 || s == -1) {
			return sl == 0 ? -s : s;
		}

		// (s == 2 | s == -2) ==> indeterminate case : computing in the form
		// |l0||l1| - |r0||r1|

		// computing absolute values
		long al0 = sl0 == 1 ? l0 : -l0;
		long al1 = sl1 == 1 ? l1 : -l1;
		long ar0 = sr0 == 1 ? r0 : -r0;
		long ar1 = sr1 == 1 ? r1 : -r1;

		int sign = crossProdSignpppp(al0, al1, ar0, ar1);
		return sl * sign;
	}

	// signed
	// absolute form
	// check exponent domination
	// check upper part
	// check overflow risk
	// bigInteger
	public static int crossProdSign1(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int s = sr0 * sr1 + sl;

		// fail fast, if sign is determined
		if (s == 0)
			return sl;

		// fail fast, if sign is determined
		if (s == 1 || s == -1) {
			return sl == 0 ? -s : s;
		}

		// (s == 2 | s == -2) ==> indeterminate case : computing in the form
		// |l0||l1| - |r0||r1|

		// computing absolute values
		long al0 = sl0 == 1 ? l0 : -l0;
		long al1 = sl1 == 1 ? l1 : -l1;
		long ar0 = sr0 == 1 ? r0 : -r0;
		long ar1 = sr1 == 1 ? r1 : -r1;

		int sign = upperPartDomination(al0, al1, ar0, ar1);
		if (sign == 0)
			sign = crossProdSignpppp(al0, al1, ar0, ar1);

		return sl * sign;
	}

	// signed
	// absolute form
	// check overflow
	// check upper part
	// check exponent domination
	// bigInteger
	public static int crossProdSign2(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int s = sr0 * sr1 + sl;

		// fail fast, if sign is determined
		if (s == 0)
			return sl;

		// fail fast, if sign is determined
		if (s == 1 || s == -1) {
			return sl == 0 ? -s : s;
		}

		// (s == 2 | s == -2) ==> indeterminate case : computing in the form
		// |l0||l1| - |r0||r1|

		// computing absolute values
		long al0 = sl0 == 1 ? l0 : -l0;
		long al1 = sl1 == 1 ? l1 : -l1;
		long ar0 = sr0 == 1 ? r0 : -r0;
		long ar1 = sr1 == 1 ? r1 : -r1;

		int ofs = ar0 > (ar1 / Long.MAX_VALUE) ? 1 : 0;
		ofs += al0 > (al1 / Long.MAX_VALUE) ? 2 : 0;

		int sign = ofs == 3 ? crossProdSignpppp2(al0, al1, ar0, ar1)
				: ofs == 0 ? crossProdSignLong(al0, al1, ar0, ar1) : 1;

		return sl * sign;
	}

	// signed
	// absolute form
	// check overflow
	// check upper part
	// check exponent domination
	// bigInteger
	public static int crossProdSign3(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int s = sr0 * sr1 + sl;

		// fail fast, if sign is determined
		if (s == 0)
			return sl;

		// fail fast, if sign is determined
		if (s == 1 || s == -1) {
			return sl == 0 ? -s : s;
		}

		// (s == 2 | s == -2) ==> indeterminate case : computing in the form
		// |l0||l1| - |r0||r1|

		// computing absolute values
		long al0 = sl0 == 1 ? l0 : -l0;
		long al1 = sl1 == 1 ? l1 : -l1;
		long ar0 = sr0 == 1 ? r0 : -r0;
		long ar1 = sr1 == 1 ? r1 : -r1;

		int sign = crossProdSignpppp2(al0, al1, ar0, ar1);

		return sl * sign;
	}

	public static int crossProdSign3bis(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int s = sr0 * sr1 + sl;

		// fail fast, if sign is determined
		if (s == 0)
			return sl;

		// fail fast, if sign is determined
		if (s == 1 || s == -1) {
			return sl == 0 ? -s : s;
		}

		// (s == 2 | s == -2) ==> indeterminate case : computing in the form
		// |l0||l1| - |r0||r1|

		// computing absolute values
		long al0 = sl0 == 1 ? l0 : -l0;
		long al1 = sl1 == 1 ? l1 : -l1;
		long ar0 = sr0 == 1 ? r0 : -r0;
		long ar1 = sr1 == 1 ? r1 : -r1;

		int sign = crossProdSignpppp3(al0, al1, ar0, ar1);

		return sl * sign;
	}

	// signed
	// absolute form
	// check overflow
	// check upper part
	// check exponent domination
	// bigInteger
	public static int crossProdSign4(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int sr = sr0 * sr1;

		int ofs = isMultOverflow(r0, r1) ? 1 : 0;
		ofs += isMultOverflow(l0, l1) ? 2 : 0;

		return ofs == 2 ? sl : ofs == 1 ? -sr : ofs == 0 ? crossProdSignLong(
				l0, l1, r0, r1) : crossProdSignBI(l0, l1, r0, r1);

	}

	public static int crossProdSign5(long l0, long l1, long r0, long r1) {

		// return sign of s = l0*l1 - r0*r1 ;

		// computing arguments sign
		int sl0 = l0 == 0 ? 0 : l0 > 0 ? 1 : -1;
		int sl1 = l1 == 0 ? 0 : l1 > 0 ? 1 : -1;
		int sr0 = r0 == 0 ? 0 : r0 > 0 ? 1 : -1;
		int sr1 = r1 == 0 ? 0 : r1 > 0 ? 1 : -1;

		int sl = sl0 * sl1;
		int sr = sr0 * sr1;
		int s = sl + sr;

		// fail fast, if sign is determined
		if (s == 0)
			return sl;

		// fail fast, if sign is determined
		if (s == 1 || s == -1)
			return sl == 0 ? -s : s;

		// (s == 2 | s == -2) ==> indeterminate case : computing in the form
		// |l0||l1| - |r0||r1|

		// computing absolute values
		long al0 = sl0 == 1 ? l0 : -l0;
		long al1 = sl1 == 1 ? l1 : -l1;
		long ar0 = sr0 == 1 ? r0 : -r0;
		long ar1 = sr1 == 1 ? r1 : -r1;

		// computing overflow signature
		int ofs = (isMultOverflowpp(al0, al1) ? 2 : 0)
				+ (isMultOverflowpp(ar0, ar1) ? 1 : 0);

		// 0 => no overflow, 1 => right only overflow, 2 => left only overflow, 3 => both overflow
		int sign = ofs == 2 ? 1 : ofs == 1 ? -1 : ofs == 0 ? crossProdSignLong(
				al0, al1, ar0, ar1) : crossProdSign_pof(al0, al1, ar0, ar1);

		return sl * sign;

	}

	private static int upperPartDomination(long l0, long l1, long r0, long r1) {

		long l0up = l0 >> 32;
		long l1up = l1 >> 32;
		long r0up = r0 >> 32;
		long r1up = r1 >> 32;

		long lup = l0up * l1up; // ensured without overflow
		long rup = r0up * r1up;
		long sign = (lup >> 32) - (rup >> 32);

		return sign > 1 ? 1 : sign < -1 ? -1 : 0;

	}

	private static int upperPartDomination2(long l0, long l1, long r0, long r1) {

		long l0up = l0 >> 32;
		long l1up = l1 >> 32;
		long r0up = r0 >> 32;
		long r1up = r1 >> 32;

		long lup = l0up * l1up; // ensured without overflow
		long rup = r0up * r1up;

		int sign = ((int) (lup >> 32)) - ((int) (rup >> 32));
		return sign > 1 ? 1 : sign < -1 ? -1 : 0;

	}

	private static int crossProdSignpppp(long l0, long l1, long r0, long r1) {

		// all arguments must be positive
		// computing right and left term's exponent dual (63 - exponent)
		int zl = Long.numberOfLeadingZeros(l0) + Long.numberOfLeadingZeros(l1);
		int zr = Long.numberOfLeadingZeros(r0) + Long.numberOfLeadingZeros(r1);

		// checking an exponent domination
		int diff = zr - zl;
		if (diff > 1)
			return 1; // left domination
		if (diff < -1)
			return -1; // right domination

		// checking overflow risk
		return zl < 65 ? (zr < 65 ? crossProdSignBI(l0, l1, r0, r1)
				: crossProdSignBILeftRisk(l0, l1, r0 * r1))
				: (zr < 65 ? -crossProdSignBILeftRisk(r0, r1, l0 * l1)
						: crossProdSignLong(l0, l1, r0, r1));

	}

	private static int crossProdSignpppp2(long l0, long l1, long r0, long r1) {

		// all arguments must be positive
		// computing right and left term's exponent dual (63 - exponent)
		int zl = Long.numberOfLeadingZeros(l0) + Long.numberOfLeadingZeros(l1);
		int zr = Long.numberOfLeadingZeros(r0) + Long.numberOfLeadingZeros(r1);

		// checking an exponent domination
		int diff = zr - zl;
		if (diff > 1)
			return 1; // left domination
		if (diff < -1)
			return -1; // right domination

		int sign = upperPartDomination2(l0, l1, r0, r1);

		if (sign == 0)
			// checking overflow risk
			sign = zl > 64 && zr > 64 ? crossProdSignLong(l0, l1, r0, r1)
					: crossProdSignBI(l0, l1, r0, r1);

		return sign;

	}

	private static int crossProdSignpppp3(long l0, long l1, long r0, long r1) {

		// all arguments must be positive
		// computing right and left term's exponent dual (63 - exponent)
		int zl = Long.numberOfLeadingZeros(l0) + Long.numberOfLeadingZeros(l1);
		int zr = Long.numberOfLeadingZeros(r0) + Long.numberOfLeadingZeros(r1);

		// checking an exponent domination
		int diff = zr - zl;
		if (diff > 1)
			return 1; // left domination
		if (diff < -1)
			return -1; // right domination

		int sign = upperPartDomination(l0, l1, r0, r1);

		if (sign == 0)
			// checking overflow risk
			sign = zl > 64 && zr > 64 ? crossProdSignLong(l0, l1, r0, r1)
					: crossProdSignBI(l0, l1, r0, r1);

		return sign;

	}

	private static int crossProdSign_pof(long l0, long l1, long r0, long r1) {

		int sign = upperPartDomination(l0, l1, r0, r1);

		if (sign == 0)
			sign = crossProdSignBI(l0, l1, r0, r1);

		return sign;

	}

	public static int crossProdSignBI(long l0, long l1, long r0, long r1) {

		BigInteger bil0 = BigInteger.valueOf(l0);

		BigInteger bil1 = BigInteger.valueOf(l1);

		BigInteger bil = bil0.multiply(bil1);

		BigInteger bir0 = BigInteger.valueOf(r0);

		BigInteger bir1 = BigInteger.valueOf(r1);

		BigInteger bir = bir0.multiply(bir1);

		return bil.compareTo(bir);

	}

	private static int crossProdSignBILeftRisk(long l0, long l1, long r) {

		// computes sign of l0*l1 - r

		BigInteger bil0 = BigInteger.valueOf(l0);

		BigInteger bil1 = BigInteger.valueOf(l1);

		BigInteger bil = bil0.multiply(bil1);

		return bil.compareTo(BigInteger.valueOf(r));

	}

	public static int crossProdSignLong(long l0, long l1, long r0, long r1) {

		long l = l0 * l1;
		long r = r0 * r1;
		return l > r ? 1 : l < r ? -1 : 0;

	}

	private static boolean isMultOverflow(long a, long b) {

		return b > 0 ? a > Long.MAX_VALUE / b || a < Long.MIN_VALUE / b
				: b < -1 ? a > Long.MIN_VALUE / b || a < Long.MAX_VALUE / b
						: a == Long.MIN_VALUE;
	}

	private static boolean isMultOverflowpp(long a, long b) {

		return a > Long.MAX_VALUE / b;
	}

}
