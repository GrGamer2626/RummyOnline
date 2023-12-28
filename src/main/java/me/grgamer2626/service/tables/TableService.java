package me.grgamer2626.service.tables;

import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.users.User;

public interface TableService {
	
	GameTable getTable(long id);
	
	boolean takeSlot(long tableId, int slot, User user);
	
	boolean leaveSlot(long tableId, int slot);
	
	void startGame(long tableId, String playerName);
	
	String endGame(long tableId, String playerName);
	
}
