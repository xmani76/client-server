package server;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Http connection handler. Offloads the client handling onto the {@link WorkerManager} class.
 *
 */
@SuppressWarnings("restriction")
public class ConnectionHandler implements HttpHandler, Closeable {
	private URI context;
	private WorkerManager workerManager;
	
	ConnectionHandler(final String context, final ServerLog log) {
		if (context == null || context.trim().isEmpty() || !context.startsWith("/")) {
			throw new IllegalArgumentException("Invalid context: " + context);
		}

		this.context = URI.create(context);
		workerManager = new WorkerManager(log);
	}
	
	public void handle(final HttpExchange exchange) throws IOException {
		String clientId = context.relativize(exchange.getRequestURI()).toString();
		if (!clientId.trim().isEmpty()) {
			workerManager.addOrUpdateWorker(clientId);
			exchange.sendResponseHeaders(200, -1);
		} else {
			// bad request
			exchange.sendResponseHeaders(400, -1);
		}
		exchange.close();
	}

	@Override
	public void close() {
		workerManager.close();
	}
}
