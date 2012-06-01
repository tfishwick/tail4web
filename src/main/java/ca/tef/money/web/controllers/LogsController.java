package ca.tef.money.web.controllers;

import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import ca.tef.money.domain.*;
import ca.tef.tail4web.services.*;

/**
 * 
 */
@Controller
public class LogsController {

	private static final Logger log = Logger.getLogger(LogsController.class);

	@Autowired
	LogService logService;

	/**
	 * Get a list of log messages for a given log collection.
	 */
	@RequestMapping(value = "/collections/{collectionName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public JsonResponse getLogCollection(@PathVariable String collectionName, HttpServletRequest request) {
		Enumeration headers = request.getHeaderNames();
		System.out.println("headers");
		while (headers.hasMoreElements()) {
			String h = (String) headers.nextElement();
			System.out.println("\t" + h + " = " + request.getHeader(h));
		}
		List<Map<String, Object>> coll = new ArrayList<Map<String, Object>>();
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("_level", "INFO");
		coll.add(doc);

		// return quote;
		JsonResponse response = new JsonResponse();
		response.getResult().put("documents", doc);
		return response;
	}

	/**
	 * Save a new log message to a collection. This submits it to a worker via rabbitmq that will do
	 * the actual processing of the message.
	 */
	@RequestMapping(value = "/collections/{collectionName}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse postLogMessage(@PathVariable String collectionName, HttpServletRequest request) {

		LogMessage doc = new LogMessage();
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			doc.put(paramName, paramValue);
		}

		logService.saveAndQueue(doc, collectionName);
		JsonResponse response = new JsonResponse();
		return response;
	}
}
