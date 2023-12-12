package me.grgamer2626.model.tables;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.users.User;
import me.grgamer2626.utils.Identifiable;

import java.util.ArrayList;
import java.util.List;

public final class GameTable implements Identifiable<Long> {
	
	private final long id;
	private User tableOwner;
	private final List<User> usersInTable;
	private final PlayerSlots playerSlots;
	private Game game;
	private int lastGameSlotStart = -1;
	
	
	
	
	
	public GameTable(long id, User tableOwner) {
		this.id = id;
		this.tableOwner = tableOwner;
		
		usersInTable = new ArrayList<>();
		usersInTable.add(tableOwner);
		
		playerSlots = new PlayerSlots();
		
		lastGameSlotStart = -1;
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//*********** Authentication ***********//
	public Long getId() {
		return id;
	}
	
	//*********** Table Attributes ***********//
	public List<User> getUsersInTable() {
		return usersInTable;
	}
	
	public boolean isEmpty() {
		return usersInTable.isEmpty();
	}
	
	public void addUser(User user) {
		if(!usersInTable.contains(user)) {
			usersInTable.add(user);
		}
	}
	
	public User getTableOwner() {
		return tableOwner;
	}
	
	public void setTableOwner(User newOwner) {
		tableOwner = newOwner;
	}
	
	public PlayerSlots getPlayerSlots() {
		return playerSlots;
	}
	
	
}
