package me.grgamer2626.model.tables;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TablesRepositoryImp implements TablesRepository {
	private final Map<Long, GameTable> tables = new ConcurrentHashMap<>();
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public Map<Long, GameTable> getRepository() {
		return tables;
	}
}
