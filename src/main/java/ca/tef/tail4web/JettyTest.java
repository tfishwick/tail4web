package ca.tef.tail4web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts
 */
class StatusService implements Runnable {
	interface Reporter {
		abstract String getStatus();
	}

	int port;
	Reporter reporter;

	long called = 0;

	public StatusService(Reporter reporter, int port) {
		this.port = port;
		this.reporter = reporter;
		Thread t = new Thread(this);

		// Daemonize 
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Create a single threaded
	 */
	public void run() {

		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			for (;;) {
				Socket client = socket.accept();
				System.out.println("new connection");

				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String line = null;
				//client.getOutputStream().write(("\r\nhi\r\n").getBytes());
				//for (;;) {
				while ((line = reader.readLine()) != null) {
					//String line = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
					//while()
					System.out.println("< " + line);
					if (line.equals(""))
						break;
				}
				System.out.println("done reading");
				client.getOutputStream().write(("HTTP/1.1 101 OK\r\n").getBytes());
				client.getOutputStream().flush();

				client.getOutputStream().write(("\r\nhi\r\n").getBytes());
				client.getOutputStream().write((reporter.getStatus() + "\r\n").getBytes());

				//}
				//client.close();
				System.out.println("done.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					System.out.println("closing socket");
					socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

public class JettyTest {
	public static void main(String[] args) throws Exception {
		System.out.println("Starting StatusService");
		new StatusService(new StatusService.Reporter() {
			@Override
			public String getStatus() {
				return "Running Threads: " + Thread.activeCount();
			}
		}, 4500);

		Thread.sleep(300000);
		System.out.println("done");

	}
}
