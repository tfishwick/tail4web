package ca.tef.tail4web;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Timer {
	long start;

	public Timer() {
		start = System.currentTimeMillis();
	}

	public String et() {
		double t = ((double) (System.currentTimeMillis() - start)) / 1000;
		BigDecimal b = new BigDecimal(t).setScale(3, RoundingMode.HALF_UP);
		start = System.currentTimeMillis();
		return b + "s";
	}

	public void reset() {
		start = System.currentTimeMillis();
	}

	public static long start() {
		return System.currentTimeMillis();
	}

	public static String elapsed(long start) {
		double t = ((double) (System.currentTimeMillis() - start)) / 1000;
		BigDecimal b = new BigDecimal(t).setScale(3, RoundingMode.HALF_UP);
		start = System.currentTimeMillis();
		return b + "s";
	}
}
