package me.grgamer2626.utils.idGenerators;

import java.util.Map;
import java.util.Set;

public interface IntegerIdGenerator<T> extends IdGenerator<T, Integer> {
	
	@Override
	default Integer generateId() {
		if(!getUnusedId().isEmpty()) {
			return getUnusedId().poll();
		}
		Map<Integer, T> repository = getRepository();
		int id = 1;
		
		if(!repository.isEmpty()) {
			int highest = getHighest(repository.keySet());
			id = ++highest;
		}
		return id;
	}
	
	private int getHighest(Set<Integer> keySet) {
		return keySet.stream()
				.mapToInt(Number::intValue)
				.max()
				.orElse(-1);
	}
}
