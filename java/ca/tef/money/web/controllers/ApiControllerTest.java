package ca.tef.money.web.controllers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import ca.tef.money.domain.LogMessage;
import ca.tef.tail4web.services.LogService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-config.xml")
public class ApiControllerTest {

	private static final Logger log = Logger.getLogger(ApiControllerTest.class);

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	LogsController controller;

	@Autowired
	private RequestMappingHandlerAdapter handlerAdapter;

	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	MockHttpServletRequest request;
	MockHttpServletResponse response;

	@Before
	public void before() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	/**
	 * Ensure that posting a log entry submits it to the service to the work queue.
	 */
	@Test
	public void testPostLogEntry() throws Exception {
		request.setRequestURI("/api/logs/test");
		request.setMethod("POST");

		HandlerMethod handler = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
		LogsController controller = (LogsController) handler.getBean();
		controller.logService = mock(LogService.class);

		handlerAdapter.handle(request, response, handler);

		verify(controller.logService, atLeastOnce()).queueMessage(any(LogMessage.class));
	}

	@Test
	public void testGetLogEntries() throws Exception {
		request.setRequestURI("/api/logs/test");
		request.setMethod("GET");

		HandlerMethod handler = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
		LogsController controller = (LogsController) handler.getBean();
		controller.logService = mock(LogService.class);

		handlerAdapter.handle(request, response, handler);

		System.out.println("model: " + response.getContentAsString());
	}

	ModelAndView doRequest(String method, String url) throws Exception {
		request.setRequestURI("/api/logs/test");
		request.setMethod("GET");

		Object handler = handlerMapping.getHandler(request).getHandler();
		return handlerAdapter.handle(request, response, handler);
	}
}
