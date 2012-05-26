import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import ca.tef.util.Timer;

public class Testing {

	@Test
	public void test2() throws Exception {
		HttpClient hc = new DefaultHttpClient();

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("http", 8080, PlainSocketFactory.getSocketFactory()));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);
		hc = new DefaultHttpClient(cm);

		Timer t = new Timer();
		HttpResponse response = hc.execute(new HttpGet("http://localhost:8080/api/logs/test"));

		String data = EntityUtils.toString(response.getEntity());
		System.out.println(data);
		System.out.println("in " + t.et());

		Thread.sleep(10000);

		t = new Timer();
		response = hc.execute(new HttpGet("http://localhost:8080/api/logs/test"));

		data = EntityUtils.toString(response.getEntity());
		System.out.println(data);
		System.out.println("in " + t.et());
	}

	@Test
	public void test1() {
		Map<Integer, Double> history = new HashMap<Integer, Double>();
		history.put(1, 9.00);
		history.put(2, 9.30);
		history.put(3, 8.88);
		history.put(4, 9.30);
		history.put(5, 8.98);
		history.put(6, 9.30);
		history.put(7, 8.98);
		history.put(8, 9.30);
		history.put(9, 8.98);
		history.put(10, 9.30);
		history.put(11, 8.98);
		history.put(12, 9.50);
		history.put(13, 8.98);
		history.put(14, 9.30);
		history.put(15, 8.97);
		history.put(16, 9.50);
		history.put(17, 8.98);
		history.put(18, 9.30);
		history.put(19, 9.50);

		double start = 30000;
		double sharePrice = 9.00;
		int shares = 3000;
		double cash = 0;

		System.out.println("Price   Market   Book   Shares  Cash");
		for (Map.Entry<Integer, Double> entry : history.entrySet()) {
			double price = entry.getValue();

			double book = shares * sharePrice;
			double market = price * shares;
			if ((market - book) > 1000) {
				System.out.println("Sell 1000");
				int sellShares = 1000 / (int) price;
				double amount = sellShares * price;
				cash += amount;
				shares -= sellShares;

			} else if (price < 8.98) {

				int buyShares = 1000 / (int) price;
				System.out.println("Buy " + buyShares);
				double amount = buyShares * price;
				cash -= amount;
				shares += buyShares;
			}

			System.out.printf("%5s %,8d %,8d %s %,8d %n", price, (int) market, (int) book, shares, (int) cash);
		}
	}

	public static void main(String[] args) {
		int startYear = 2012;
		int endYear = 2022;

		Fund fund = new Fund();
		// fund.setQuarterlyDividend(.05);

		BigDecimal initial = B(10000);
		BigDecimal div = B(0.005); // .5 % /month

		BigDecimal pricePerShare = B(12.50);
		// BigDecimal monthlyContribution

		BigDecimal currentAmount = initial;
		System.out.println("Amount");
		DateTime dt = new DateTime(startYear, 1, 1, 0, 0);
		for (int year = startYear; year < endYear; year++) {

			for (int month = 0; month < 12; month++) {

				// Fetch the dividend for the given year/month from the fund
				// thingy
				BigDecimal dividend = currentAmount.multiply(div);
				fund.dividend(currentAmount, dt);

				System.out.printf("%s %10s %8s %5s", year, month, currentAmount.setScale(2, RoundingMode.HALF_UP),
						dividend.setScale(2, RoundingMode.HALF_UP));
				System.out.println("");

				currentAmount = currentAmount.add(dividend);

				dt = dt.plusMonths(1);
			}
		}
	}

	static BigDecimal B(double amount) {
		return BigDecimal.valueOf(amount);
	}
}

class BigDecimalUtil {
	static BigDecimal B(double amount) {
		return BigDecimal.valueOf(amount);
	}

}

class Fund {
	// float quarterlyDividend

	public BigDecimal dividend(BigDecimal amount, DateTime dt) {
		// Quarters are 3, 6, 9, 12
		int month = dt.getMonthOfYear();
		System.out.println("month=" + month);
		if (month == 3 || month == 6 || month == 9 || month == 12) {
			System.out.print(dt.getMonthOfYear() + " is a quarter.");
			return BigDecimal.valueOf(0.5);
		} else {
			return BigDecimal.valueOf(0.5);
		}
	}
}

class Dividend {
	enum Schedule {
		MONTHLY, YEARLY, QUARTERLY
	};

	Schedule schedule;

	BigDecimal rate;

	public Dividend(Schedule schedule, BigDecimal rate) {

	}
}
