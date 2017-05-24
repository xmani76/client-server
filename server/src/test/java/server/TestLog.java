package server;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

class TestLog extends ServerLog {
	public Map<String, Integer> clientCounters = new HashMap<>();
	
	@Override
	public void log(String clientId, int counter) {
		System.out.println("clientId:"+clientId+",counter:"+counter);
		if (clientCounters.containsKey(clientId)) {
			assertTrue(counter == 0 || counter == clientCounters.get(clientId)+1);
		}
		
		clientCounters.put(clientId, counter);
	}
	
	public void clear() {
		clientCounters.clear();
	}
}