package ca.tef.tail4web;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class WebSocketServlet extends org.eclipse.jetty.websocket.WebSocketServlet {

	private final Set<TailerSocket> members = new CopyOnWriteArraySet<TailerSocket>();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
		System.out.println("socket connect");
		return new TailerSocket(members);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("forward");
		getServletContext().getNamedDispatcher("default").forward(request,
				response);
	}

	class Tailer implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
	}

	static class TailerSocket implements WebSocket.OnTextMessage {
		private static final Logger log = Logger.getLogger(TailerSocket.class);

		private Connection _connection;

		final Set<TailerSocket> members;

		String collectionName;

		public TailerSocket(Set<TailerSocket> members) {
			this.members = members;
		}

		@Override
		public void onClose(int closeCode, String message) {
			members.remove(this);
			log.info("Connections: " + members.size());
		}

		public void sendMessage(String data) throws IOException {
			_connection.sendMessage(data);
		}

		@Override
		public void onMessage(String data) {
			//sendMessage("Got " + data);
			BasicDBObject json = (BasicDBObject) JSON.parse(data);
			String method = (String) json.get("method");
			BasicDBObject params = (BasicDBObject) json.get("params");
			doTail((String) params.get("collectionName"));
		}

		public boolean isOpen() {
			return _connection.isOpen();
		}

		@Override
		public void onOpen(Connection connection) {
			members.add(this);
			log.info("Connections: " + members.size());

			_connection = connection;

		}

		public void doTail(String collectionName) {
			// Start a thread that will tail a given log file.
			TailerThread tailerThread = ApplicationContextProvider.getContext().getAutowireCapableBeanFactory()
					.createBean(TailerThread.class);
			tailerThread.collectionName = collectionName;
			tailerThread.documentHandler = new TailerThread.DocumentHandler() {
				@Override
				public void onDocument(DBObject doc) {
					try {
						_connection.sendMessage(JSON.serialize(doc.toMap()));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			};
			tailerThread.start();
		}
	}

}
