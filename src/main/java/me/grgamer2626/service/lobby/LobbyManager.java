package me.grgamer2626.service.lobby;

import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.tables.TablesRepository;
import me.grgamer2626.model.users.User;
import me.grgamer2626.utils.memoryRepository.exceptions.RepositoryException;
import me.grgamer2626.utils.scheduler.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

@Service
public class LobbyManager implements LobbyService {
	
	private final Queue<Long> unusedId;
	private final TablesRepository tablesRepository;
	private final Scheduler scheduler;
	
	@Autowired
	public LobbyManager(TablesRepository tablesRepository, Scheduler scheduler) {
		this.tablesRepository = tablesRepository;
		this.scheduler = scheduler;
		this.unusedId = new PriorityQueue<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public Queue<Long> getUnusedId() {
		return unusedId;
	}
	
	@Override
	public Map<Long, GameTable> getTables() {
		return tablesRepository.getRepository();
	}
	
	@Override
	public GameTable createTable(User tableOwner) throws RepositoryException {
		return createTable(generateId(), tableOwner);
	}
	
	private GameTable createTable(long id, User user) throws RepositoryException {
		return tablesRepository.save(new GameTable(id, user, scheduler));
	}
	@Override
	public GameTable getTable(long id) {
		return tablesRepository.findById(id);
	}
	
	@Override
	public void removeTable(long id) {
		tablesRepository.removeById(id);
		
		if(!unusedId.contains(id)) {
			unusedId.add(id);
		}
	}
	
	
}
