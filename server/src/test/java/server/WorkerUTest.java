package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WorkerUTest {

	@Test
	public void test() throws Exception {
		Worker worker = new Worker("xyz", new TestLog());
		int start = worker.getCounter();
		Thread.sleep(5000);
		worker.stop();
		
		assertTrue(worker.getCounter() - start >= 5 );
		assertEquals("xyz", worker.getId());
	}
}
