package test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class YqlDemo {

	/**
	 * Find 'food' places for the JPR
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String request = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20query%3D%22food%22%20and%20location%3D%22crested%20butte%2C%20co%22&format=xml";
		String yql = "select * from yahoo.finance.quotes where symbol in ('vab.to', 'vsb.to', 'vce.to')";
		request = "http://query.yahooapis.com/v1/public/yql";

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("q", yql));
		qparams.add(new BasicNameValuePair("env", "store://datatables.org/alltableswithkeys"));
		qparams.add(new BasicNameValuePair("format", "xml"));

		String queryString = URLEncodedUtils.format(qparams, "UTF-8");

		String uri = "http://query.yahooapis.com/v1/public/yql?" + queryString;

		HttpGet httpget = new HttpGet(uri);

		HttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(uri);

		HttpResponse hresponse = client.execute(method);
		if (hresponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + hresponse.getStatusLine());
			hresponse.getEntity().writeTo(System.out);
		} else {
			// hresponse.getEntity().getContent()
			InputStream rstream = null;
			rstream = hresponse.getEntity().getContent();
			// Process response
			Document response = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(rstream);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			// Get all search Result nodes
			NodeList nodes = (NodeList) xPath.evaluate("query/results/Result", response, XPathConstants.NODESET);
			int nodeCount = nodes.getLength();
			// iterate over search Result nodes
			for (int i = 0; i < nodeCount; i++) {
				// Get each xpath expression as a string
				String title = (String) xPath.evaluate("Title", nodes.item(i), XPathConstants.STRING);
				String summary = (String) xPath.evaluate("Address", nodes.item(i), XPathConstants.STRING);
				String url = (String) xPath.evaluate("Url", nodes.item(i), XPathConstants.STRING);
				// print out the Title, Summary, and URL for each search result
				System.out.println("Title: " + title);
				System.out.println("Address: " + summary);
				System.out.println("URL: " + url);
				System.out.println("-----------");

			}
		}
	}

}
