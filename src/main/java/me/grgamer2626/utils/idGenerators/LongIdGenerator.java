package me.grgamer2626.utils.idGenerators;

import java.util.Map;
import java.util.Set;

public interface LongIdGenerator<T> extends IdGenerator<T, Long> {
	
	@Override
	default Long generateId() {
		if(!getUnusedId().isEmpty()) {
			return getUnusedId().poll();
		}
		Map<Long, T> repository = getRepository();
		long id = 1L;
		
		if(!repository.isEmpty()) {
			long highest = getHighest(repository.keySet());
			id = ++highest;
		}
		return id;
	}
	
	
	private long getHighest(Set<Long> keySet) {
		return keySet.stream()
				.mapToLong(Number::longValue)
				.max()
				.orElse(-1);
	}
}
