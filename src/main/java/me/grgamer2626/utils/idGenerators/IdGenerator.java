package me.grgamer2626.utils.idGenerators;

import java.util.Map;
import java.util.Queue;

public interface IdGenerator<T, ID extends Number> {
	
	/**
	 * Generate id number.
	 *
	 * @return generated id
	 */
	ID generateId();
	
	/**
	 * A queue with id numbers that have been generated but are no longer used.
	 *
	 * @return queue with unused ids
	 */
	Queue<ID> getUnusedId();
	
	
	/**
	 * Returns the repository.
	 *
	 * @return the repository map
	 */
	Map<ID, T> getRepository();
	
	
	
}
