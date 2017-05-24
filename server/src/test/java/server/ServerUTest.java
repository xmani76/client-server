package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ServerUTest {
	@Parameters
	public static List<Object[]> parameters() { 
		return Arrays.asList(
				new Object[] {0, IllegalArgumentException.class, 0},
				new Object[] {-1, IllegalArgumentException.class, 0},
				new Object[] {3456, IllegalArgumentException.class, 0},
				new Object[] {100000, IllegalArgumentException.class, 0},
				new Object[] {10000, null, 10000},
				new Object[] {65535, null, 65535},
				new Object[] {12000, null, 12000}
		);
	}
	
	@Parameter(0)
	public int port;

	@Parameter(1)
	public Class<? extends Exception> thrownExceptionClass;
				
	@Parameter(2)
	public int expectedPort;
	
	@Test
	public void test() {
		Server s = null;
		try {
			s = new Server(port, new TestLog());
			assertNotNull(s);
			if (thrownExceptionClass != null) {
				fail(thrownExceptionClass + " not thrown");
			}
		} catch (Exception e) {
			assertEquals(thrownExceptionClass, e.getClass());
		} finally {
			if (s != null) {
				s.stop();
			}
		}
	}
}
