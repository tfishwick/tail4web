package ca.tef.money.web.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.tef.tail4web.services.Ipn;
import ca.tef.tail4web.services.IpnService;

@Controller
public class IpnController {

	@Autowired
	IpnService ipnService;

	private static final Logger log = Logger.getLogger(IpnController.class);

	/**
	 * Validate and process a PayPal IPN request.
	 */
	@RequestMapping(value = "/ipns", method = RequestMethod.POST)
	@ResponseBody
	public String saveIpn(HttpServletRequest request) throws Exception {
		Ipn ipn = createAndValidateIpnFromRequest(request);
		log.info("Valid IPN: " + ipn.params);
		ipnService.save(ipn);

		return "";
	}

	Ipn createAndValidateIpnFromRequest(HttpServletRequest request) throws IOException {
		log.info("Validating IPN Request " + request);
		Enumeration<String> en = request.getParameterNames();
		String str = "cmd=_notify-validate";
		Ipn ipn = new Ipn();
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);

			str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue);

			ipn.params.put(paramName, paramValue);
		}

		URL u = new URL("https://developer.paypal.com/us/cgi-bin/devscr");
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(str);
		pw.close();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(uc.getInputStream()));
		String res = in.readLine();
		in.close();

		// assign posted variables to local variables
		String itemName = request.getParameter("item_name");
		String itemNumber = request.getParameter("item_number");
		String paymentStatus = request.getParameter("payment_status");
		String paymentAmount = request.getParameter("mc_gross");
		String paymentCurrency = request.getParameter("mc_currency");
		String txnId = request.getParameter("txn_id");
		String receiverEmail = request.getParameter("receiver_email");
		String payerEmail = request.getParameter("payer_email");

		// check notification validation
		if (res.equals("VERIFIED")) {
			return ipn;
		} else if (res.equals("INVALID")) {
			log.error("IPN Validation failed, params: " + ipn.params);
			throw new RuntimeException("IPN Invalid.");
		} else {
			throw new RuntimeException("Unknown response from IPN validation.");
		}
	}

	/**
	 * Return a specific IPN.
	 */
	@RequestMapping(value = "/ipns", method = RequestMethod.GET)
	@ResponseBody
	public String getIpn() {
		return "ist of IPN's";
	}
}
