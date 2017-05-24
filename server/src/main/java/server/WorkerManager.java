package server;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class WorkerManager implements Closeable {
	private Timer cleaner;
	private Collection<String> liveWorkerIds;
	private Map<String, Worker> workers;
	private ServerLog log;
	
	WorkerManager(final ServerLog log) {
		liveWorkerIds = new ConcurrentSkipListSet<>();
		workers = new ConcurrentHashMap<>();
		this.log = log;
		cleaner = new Timer();
		cleaner.schedule(new TimerTask() {

			@Override
			public void run() {				
				for (Worker worker: getWorkers().values()) {
					if (!getLiveWorkerIds().contains(worker.getId())) {
						worker.stop();
						getWorkers().remove(worker.getId());
					}
				}
				getLiveWorkerIds().clear();
			}
			
		}, 5000, 5000);					
	}
		
	public void addOrUpdateWorker(final String workerId) {
		if (!liveWorkerIds.contains(workerId)) {
			liveWorkerIds.add(workerId);
		}
		if (!workers.containsKey(workerId)) {
			workers.put(workerId, new Worker(workerId, log));
		}
	}

	public Collection<String> getLiveWorkerIds() {
		return liveWorkerIds;
	}

	public Map<String, Worker> getWorkers() {
		return workers;
	}

	@Override
	public void close() {
		for (Worker worker: getWorkers().values()) {
			worker.stop();
		}
		
		getWorkers().clear();
		getLiveWorkerIds().clear();
	}
}
