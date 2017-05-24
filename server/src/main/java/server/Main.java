package server;

/**
 * Application execution entry point. 
 *
 */
public final class Main {
	public static void main(String[] args) {
		try {
			if (args == null || args.length != 1) {
				throw new IllegalArgumentException(Server.INVALID_PORT_ERROR);
			} else {			
				try {
					int port = Integer.parseInt(args[0]);
					final Server s = new Server(port, new ServerLog());
					Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
						public void run() {
							s.stop();
						}
					}));
				} catch(NumberFormatException e) {
					throw new IllegalArgumentException(Server.INVALID_PORT_ERROR);
				}
			}
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	/**
	 * Private on purpose to prevent instantiation.
	 */
	private Main() {
		// do nothing
	}
}
