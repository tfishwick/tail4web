package ca.tef.tail4web.services;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ca.tef.util.Timer;

class Symbol {
	String symbol;

	String stockExchange;

	String yahooSymbol;

	String googleSymbol;
}

class SymbolsUtil {

	static List<String> normalize(List<String> symbols) {
		List<String> normalizedSymbols = new ArrayList<String>();
		for (String symbol : symbols) {
			symbol = symbol.toUpperCase();
			if (symbol.endsWith(".TO")) {
				symbol = "TSE:" + symbol.substring(0, symbol.length() - 3);
			}
			normalizedSymbols.add(symbol);
		}
		return normalizedSymbols;
	}

	static List<String> toYahoo(List<String> symbols) {
		List<String> yahoo = new ArrayList<String>();
		for (String symbol : symbols) {
			if (symbol.startsWith("TSE:")) {
				symbol = symbol.substring(4) + ".TO";
			}
			yahoo.add(symbol);
		}
		return yahoo;
	}
}

@Service
public class MultiQuoteService {

	// @Autowired
	// YqlDao

	private static final Logger log = Logger.getLogger(MultiQuoteService.class);

	static {
		// Execute a job each hour to clear the cache.
		// log.

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

		exec.schedule(new Runnable() {
			public void run() {
				log.debug("clearing cache");
				cache.clear();
			}
		}, 15, TimeUnit.MINUTES);
		log.info("Started executor to clear symbols cache every 15 minutes.");
	}

	private static Map<String, Quote> cache = new HashMap<String, Quote>();

	public List<Quote> getQuotes(List<String> symbols) throws Exception {
		long start = Timer.start();

		// Convert symbols into common format.
		List<String> normalizedSymbols = SymbolsUtil.normalize(symbols);

		// We'll return this list of quotes.
		List<Quote> quotes = new ArrayList<Quote>();

		// Remove cached quotes from symbols request list.
		Iterator<String> it = normalizedSymbols.iterator();
		while (it.hasNext()) {
			String symbol = it.next();
			if (cache.containsKey(symbol)) {
				log.info("Removed cached symbol: " + symbol);
				it.remove();
				quotes.add(cache.get(symbol));
			}
		}

		quotes.addAll(getYahooQuotes(normalizedSymbols));

		log.info("Returning " + quotes.size() + " in " + Timer.elapsed(start));
		return quotes;
	}

	List<Quote> getYahooQuotes(List<String> symbols) throws Exception {
		List<Quote> quotes = new ArrayList<Quote>();

		if (symbols.isEmpty()) {
			return quotes;
		}
		String symbolsQuoted = "'" + StringUtils.join(SymbolsUtil.toYahoo(symbols), "', '") + "'";

		String yql = "select * from yahoo.finance.quotes where symbol in (" + symbolsQuoted + ")";
		log.debug("YQL Query: " + yql);
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("q", yql));
		qparams.add(new BasicNameValuePair("env", "store://datatables.org/alltableswithkeys"));
		qparams.add(new BasicNameValuePair("format", "xml"));

		String queryString = URLEncodedUtils.format(qparams, "UTF-8");

		String uri = "http://query.yahooapis.com/v1/public/yql?" + queryString;

		HttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(uri);

		HttpResponse hresponse = client.execute(method);

		if (hresponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

			hresponse.getEntity().writeTo(System.out);
		} else {
			// hresponse.getEntity().getContent()
			InputStream rstream = hresponse.getEntity().getContent();
			String responseText = IOUtils.toString(rstream);

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(responseText));

			// Process response
			Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			// Get all search Result nodes
			NodeList nodes = (NodeList) xPath.evaluate("//quote", response, XPathConstants.NODESET);
			int nodeCount = nodes.getLength();
			// iterate over search Result nodes

			for (int i = 0; i < nodeCount; i++) {
				Node node = nodes.item(i);

				String symbol = (String) xPath.evaluate("Symbol", node);
				String err = (String) xPath.evaluate("ErrorIndicationreturnedforsymbolchangedinvalid", node);
				if (err != null && err.length() > 0) {
					throw new RuntimeException(err);
				}
				String lastTradePrice = (String) xPath.evaluate("LastTradePriceOnly", node);
				String exchange = (String) xPath.evaluate("StockExchange", node);
				// String exchange = xml.getStringRequired("StockExchange");
				String marketCapStr = (String) xPath.evaluate("MarketCapitalization", node);
				BigDecimal marketCap;
				if (marketCapStr.endsWith("B")) {
					marketCap = new BigDecimal(marketCapStr.substring(0, marketCapStr.length() - 1));
				} else {
					marketCap = BigDecimal.valueOf(0);
					// throw new
					// RuntimeException("Unable to convert marketCap: " +
					// marketCapStr);
				}

				Quote quote = new Quote();
				quote.setSymbol(symbol);
				quote.setExchange(exchange);
				quote.setMarketCapitalization(marketCap);
				quote.setLastTradePrice(new BigDecimal(lastTradePrice));
				quotes.add(quote);

				if (quote.getExchange().equalsIgnoreCase("toronto")) {
					symbol = "TSE:" + symbol.substring(0, symbol.length() - 3);
				}
				log.debug("Cached: " + symbol + " = " + quote.getLastTradePrice());
				cache.put(symbol, quote);
			}
		}
		return quotes;
	}
}
