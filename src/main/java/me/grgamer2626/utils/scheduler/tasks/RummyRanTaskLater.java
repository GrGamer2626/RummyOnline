package me.grgamer2626.utils.scheduler.tasks;

public class RummyRanTaskLater extends RummyTask {
	
	private final Runnable runnable;
	private final long delay;
	private boolean stopTask;
	
	
	public RummyRanTaskLater(int id, Runnable runnable, long delay) {
		super(id);
		this.runnable = runnable;
		this.delay = delay;
		
		this.stopTask = false;
	}
	
	@Override
	public void run() {
		long startTime = System.currentTimeMillis() + delay;
		while(startTime >= System.currentTimeMillis()) {
			if(stopTask) return;
		}
		runnable.run();
	}
	
	@Override
	public void stop() {
		stopTask = true;
	}
	
	
}
