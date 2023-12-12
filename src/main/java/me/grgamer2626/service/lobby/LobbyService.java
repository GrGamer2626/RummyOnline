package me.grgamer2626.service.lobby;

import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.users.User;
import me.grgamer2626.utils.idGenerators.LongIdGenerator;
import me.grgamer2626.utils.memoryRepository.exceptions.RepositoryException;

import java.util.Map;

public interface LobbyService extends LongIdGenerator<GameTable> {
	
	@Override
	default Map<Long, GameTable> getRepository() {
		return getTables();
	}
	
	Map<Long, GameTable> getTables();
	
	GameTable createTable(User lobbyOwner) throws RepositoryException;
	
	GameTable getTable(long id);
	
	void removeTable(long id);
	
	
}
