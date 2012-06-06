package ca.tef.tail4web;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/app-config.xml")
public class CollectionsControllerTest {

	@Autowired
	ApplicationContext applicationContext;

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
		request.setRequestURI("/collections/test");
		request.setMethod("POST");

		HandlerMethod handler = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
		LogsController controller = (LogsController) handler.getBean();
		controller.logService = mock(LogService.class);

		handlerAdapter.handle(request, response, handler);

		verify(controller.logService, atLeastOnce()).saveAndQueue(any(LogMessage.class), "test");
	}

	@Test
	public void testGetLogEntries() throws Exception {
		request.setRequestURI("/collections/test");
		request.setMethod("GET");

		HandlerMethod handler = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
		LogsController controller = (LogsController) handler.getBean();
		controller.logService = mock(LogService.class);

		handlerAdapter.handle(request, response, handler);

		System.out.println("model: " + response.getContentAsString());
	}

	ModelAndView doRequest(String method, String url) throws Exception {
		request.setRequestURI("/collections/test");
		request.setMethod("GET");

		Object handler = handlerMapping.getHandler(request).getHandler();
		return handlerAdapter.handle(request, response, handler);
	}
}
