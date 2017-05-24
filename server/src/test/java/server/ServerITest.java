package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerITest {

	private Server server;
	private TestLog log;
	
	@Before
	public void setup() throws Exception {
		log = new TestLog();
		server = new Server(16666, log);
	}
	
	@After
	public void teardown() {
		server.stop();
		log = null;
	}
	
	/**
	 * Verify we don't log anything if there have been ZERO client connections.
	 * @throws Exception
	 */
	@Test
	public void noClients() throws Exception {
		// sleep for a couple of seconds
		Thread.sleep(2000);
		
		// verify nothing exists in the log
		assertTrue(log.clientCounters.isEmpty());
	}
	
	/**
	 * Verify proper logging when we have multiple unique client connections.
	 * 
	 * @throws Exception
	 */
	@Test
	public void multipleUniqueClients() throws Exception {
		URL base = new URL("http://localhost:16666/ping/");
		final int NUM_CLIENTS = 4;
		
		for (int i = 0 ; i < NUM_CLIENTS ; i++) {
			URL url = new URL(base, String.valueOf(i));
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			assertEquals(200, conn.getResponseCode());
		}
		
		// sleep for a couple of seconds
		Thread.sleep(2000);
		
		assertEquals(NUM_CLIENTS, log.clientCounters.keySet().size());
	}

	/**
	 * Verify proper logging when multiple clients connect with the same client ID.
	 * 
	 * @throws Exception
	 */
	@Test
	public void multipleDupClientConnections() throws Exception {
		URL base = new URL("http://localhost:16666/ping/");
		final int NUM_CLIENTS = 4;

		for (int i = 0 ; i < NUM_CLIENTS ; i++) {
			URL url = new URL(base, String.valueOf(i%2));
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			assertEquals(200, conn.getResponseCode());
		}
		
		// sleep for a couple of seconds
		Thread.sleep(2000);
		
		assertEquals(2, log.clientCounters.keySet().size());
	}


	/**
	 * Test that nothing is logged after a client is disconnected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void clientConnDrop() throws Exception {
		URL base = new URL("http://localhost:16666/ping/");
		
		URL url = new URL(base, "1");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		assertEquals(200, conn.getResponseCode());
		
		Thread.sleep(5000);
		assertEquals(1, log.clientCounters.keySet().size());
	
		Thread.sleep(6000);
		assertEquals(1, log.clientCounters.keySet().size());
		log.clear();
		
		Thread.sleep(2000);
		assertTrue(log.clientCounters.isEmpty());
	}

	/**
	 * Verify that counter resets when a client reconnects.
	 * 
	 * @throws Exception
	 */
	@Test
	public void clientReconnect() throws Exception {
		URL base = new URL("http://localhost:16666/ping/");
		
		URL url = new URL(base, "1");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		assertEquals(200, conn.getResponseCode());
		
		Thread.sleep(2000);
		assertEquals(1, log.clientCounters.keySet().size());
		Thread.sleep(9000);
		log.clear();
		Thread.sleep(2000);
		assertTrue(log.clientCounters.isEmpty());
	
		conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		assertEquals(200, conn.getResponseCode());
		Thread.sleep(2000);
		assertEquals(1, log.clientCounters.keySet().size());
	}
}
