package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * Create an HTTP server.
 *
 */
@SuppressWarnings("restriction")
public class Server {	
	private static final int MIN_PORT = 10000;
	private static final int MAX_PORT = 65535;
	
	public static final String INVALID_PORT_ERROR = 
			"Please specify a port number between " + MIN_PORT + " and " + MAX_PORT + " (both inclusive).";

	private static final String CONTEXT = "/ping/";
	private HttpServer server;
	private ConnectionHandler handler;
	
	/**
	 * Visible for testing.
	 * 
	 * @param port
	 */
	Server(final int port, final ServerLog log) throws IOException {
		if (port < MIN_PORT || port > MAX_PORT) {
			throw new IllegalArgumentException(INVALID_PORT_ERROR);
		}
		
		handler = new ConnectionHandler(CONTEXT, log);
		server = HttpServer.create(
				new InetSocketAddress("localhost", port), 100);
		server.createContext(CONTEXT, handler);
		server.start();
	}
	
	public void stop() {
		handler.close();
		server.stop(0);
	}
}
