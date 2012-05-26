package ca.tef.money.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.tef.tail4web.services.*;

@Controller
@RequestMapping("/quotes")
public class QuotesController {

	@Autowired
	MultiQuoteService multiSymbolService;

	@RequestMapping(value = "{symbols}", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse getQuote(@PathVariable List<String> symbols) {
		try {
			System.out.println("=" + symbols);
			// List<String> symbols = new ArrayList<String>();
			// for (String symbol : StringUtils.split(symbolList, ',')) {
			// symbols.add(symbol);
			// }
			// System.out.println("symbols: " + symbols);
			List<Quote> quotes = multiSymbolService.getQuotes(symbols);
			// return quote;
			JsonResponse response = new JsonResponse();
			response.getResult().put("quotes", quotes);
			return response;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Object getTest() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("quote", new Quote());
		return map;
	}
}
