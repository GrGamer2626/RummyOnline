package me.grgamer2626.utils.scheduler.tasks;

public class RummyRunTaskTimer extends RummyTask {
	
	private boolean stopTask;
	private final Runnable runnable;
	private final long duration;
	private final long delay;
	
	public RummyRunTaskTimer(int id, Runnable runnable, long delay, long duration) {
		super(id);
		this.stopTask = false;
		this.runnable = runnable;
		this.delay = delay;
		this.duration = duration;
		
	}
	
	@Override
	public void run() {
		long stopTime = System.currentTimeMillis() + duration;
		
		while(stopTime >= System.currentTimeMillis()) {
			if(stopTask) break;
			
			runnable.run();
			
			try {
				Thread.sleep(delay);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	@Override
	public void stop() {
		stopTask = true;
	}
}
