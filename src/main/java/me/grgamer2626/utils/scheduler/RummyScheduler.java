package me.grgamer2626.utils.scheduler;


import me.grgamer2626.utils.scheduler.tasks.RummyRanTaskLater;
import me.grgamer2626.utils.scheduler.tasks.RummyRunTaskTimer;
import me.grgamer2626.utils.scheduler.tasks.RummyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RummyScheduler implements Scheduler {
	
	private final Queue<Integer> unusedId;
	private final Map<Integer, RummyTask> runningTasks;
	private TaskExecutor taskExecutor;
	
	

	
	@Autowired
	public RummyScheduler(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
		this.unusedId = new PriorityQueue<>();
		this.runningTasks = new ConcurrentHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	
	@Override
	public Map<Integer, RummyTask> getRepository() {
		return runningTasks;
	}
	
	@Override
	public Queue<Integer> getUnusedId() {
		return unusedId;
	}
	
	@Override
	public synchronized Integer generateId() {
		return Scheduler.super.generateId();
	}
	
	@Override
	public int runTaskTimer(Runnable runnable, long delay, long duration) {
		int taskId = generateId();
		
		RummyTask rummyTask = new RummyRunTaskTimer(taskId, runnable, delay, duration);
		taskExecutor.execute(rummyTask);
		
		runningTasks.put(taskId, rummyTask);
		
		return taskId;
	}
	
	@Override
	public int runTaskLater(Runnable runnable, long delay) {
		int taskId = generateId();
		
		RummyTask rummyTask =new RummyRanTaskLater(taskId, runnable, delay);
		taskExecutor.execute(rummyTask);
		
		runningTasks.put(taskId, rummyTask);
		
		return taskId;
	}
	
	@Override
	public RummyTask getTask(int taskId) {
		return runningTasks.get(taskId);
	}
	
	@Override
	public int stopTask(int taskId) {
		RummyTask task = getTask(taskId);
		
		if(task == null) throw new IllegalArgumentException("The task with id "+taskId+" does not exist.");
		
		task.stop();
		runningTasks.remove(taskId);
		
		return -1;
	}
	
	
	
}
