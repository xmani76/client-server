package server;

public class ServerLog {
	public void log(String clientId, int counter) {
		System.out.println(new StringBuilder("Client:").
				append(clientId).append(",counter:").append(counter).toString());
	}
}
