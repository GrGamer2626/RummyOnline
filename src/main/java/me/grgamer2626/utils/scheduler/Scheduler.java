package me.grgamer2626.utils.scheduler;

import me.grgamer2626.utils.idGenerators.IntegerIdGenerator;
import me.grgamer2626.utils.scheduler.tasks.RummyTask;

public interface Scheduler extends IntegerIdGenerator<RummyTask> {
	
	
	int runTaskTimer(Runnable runnable, long delay, long period);
	
	int runTaskLater(Runnable runnable, long delay);
	
	RummyTask getTask(int id);
	
	int stopTask(int taskId);
}
