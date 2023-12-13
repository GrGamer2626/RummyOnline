package me.grgamer2626.utils.scheduler.tasks;



public abstract class RummyTask implements Runnable {
	private final int id;
	
	public RummyTask(int id) {
		this.id = id;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public int id() {
		return id;
	}
	
	public abstract void stop();

	

}
