package server;

import java.util.Timer;
import java.util.TimerTask;

public class Worker {
	private Timer timer;
	private volatile int counter;
	private String id;
	
	Worker(final String client, final ServerLog log) {
		id = client;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Worker.this.setCounter(Worker.this.getCounter()+1);
				log.log(getId(), getCounter()); 
			}
		}, 0, 1000);
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public String getId() {
		return id;
	}
	
	public void setCounter(final int value) {
		this.counter = value;
	}
	
	public int getCounter() {
		return this.counter;
	}
}
