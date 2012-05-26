package ca.tef.money.web.controllers;

import java.util.*;

import javax.servlet.http.*;

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

	// private static final Logger logger;

	@Autowired
	LogService logService;

	/**
	 * Get a list of log messages for a given log collection.
	 */
	@RequestMapping(value = "/api/logs/{collName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public JsonResponse getLogCollection(@PathVariable String collName, HttpServletRequest request) {
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
	@RequestMapping(value = "/api/logs/{collName}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse postLogMessage(@PathVariable String collName, HttpServletRequest request) {
		LogMessage logMessage = new LogMessage();

		logService.queueMessage(logMessage);

		JsonResponse response = new JsonResponse();
		return response;
	}
}
