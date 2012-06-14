package ca.tef.tail4web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	final public static String docRoot = System.getProperty("user.dir") + "/htdocs";

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		Server server = new Server(8080);

		WebAppContext context = new WebAppContext();
		//context.setDescriptor("/WEB-INF/web.xml");
		context.setResourceBase("src/main/webapp");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);

		server.setHandler(context);

		server.start();
		server.join();
	}
}