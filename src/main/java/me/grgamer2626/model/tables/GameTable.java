package me.grgamer2626.model.tables;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.decks.Deck;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.tables.TableService;
import me.grgamer2626.service.websocket.WebSocketService;
import me.grgamer2626.utils.Identifiable;
import me.grgamer2626.model.tables.utils.StartCountDown;
import me.grgamer2626.utils.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;

public final class GameTable implements Identifiable<Long> {
	
	private final long id;
	private User tableOwner;
	private final List<User> usersInTable;
	private final PlayerSlots playerSlots;
	private Game game;
	private int lastGameSlotStart;
	private final Scheduler scheduler;
	private int startingCountDownTaskId;
	
	
	public GameTable(long id, User tableOwner, Scheduler scheduler) {
		this.id = id;
		this.tableOwner = tableOwner;
		this.scheduler = scheduler;
		
		usersInTable = new ArrayList<>();
		usersInTable.add(tableOwner);
		
		playerSlots = new PlayerSlots();
		
		lastGameSlotStart = -1;
		startingCountDownTaskId = -1;
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
	
	public Game getGame() {
		return game;
	}
	
	public boolean isGameStarted() {
		return game != null;
	}
	
	public Game startGame() {
		if(isGameStarted()) return game;
		
		if(playerSlots.getPlayerAmount() < 2) return null;
		
		Deck deck = new Deck();
		game = new Game(id, deck, playerSlots, lastGameSlotStart);
		lastGameSlotStart = game.getCurrentTurnPlayer();
		
		return game;
	}
	
	public Game endGame() {
		if(!isGameStarted()) return null;
		Game temp = game;
		game = null;
		
		return temp;
	}
	
	
	public void startStartingCountDown(WebSocketService webSocketService, TableService tableService) {
		long duration = 10_000L;
		startingCountDownTaskId = scheduler.runTaskTimer(new StartCountDown(id, duration, webSocketService, tableService), 1000L, (duration + 1000));
	}
	
	public void stopStartingCountDown() {
		startingCountDownTaskId = scheduler.stopTask(startingCountDownTaskId);
	}
	
	public int getStartingCountDownTaskId() {
		return startingCountDownTaskId;
	}
	
	public void setStartingCountDownTaskId(int startingCountDownTaskId) {
		this.startingCountDownTaskId = startingCountDownTaskId;
	}
	
	
	
	public void startWaitCountDown(Scheduler scheduler) {
		//TODO
	}
	
	public void stopWaitCountDown(Scheduler scheduler) {
		//TODO
	}
}
