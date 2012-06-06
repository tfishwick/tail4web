package ca.tef.tail4web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 */
@Controller
public class LogsController {

	private static final Logger log = Logger.getLogger(LogsController.class);

	@Autowired
	public LogService logService;

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

	@RequestMapping(value = "/logs", method = RequestMethod.GET)
	public ModelAndView showHeatmap() {

		ModelAndView mov = new ModelAndView("logs");

		// Add the list of document collections available to tail/view.
		mov.addObject("collections", logService.findCollections());

		return mov;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView model = new ModelAndView("index");
		model.addObject("collections", logService.findCollections());
		return model;
	}

	@RequestMapping(value = "/{collectionName}", method = RequestMethod.GET)
	public ModelAndView showWebsockets(@PathVariable String collectionName) {
		ModelAndView model = new ModelAndView("tail");
		model.addObject("collection", logService.findCollection(collectionName));
		return model;
	}
}
