package me.grgamer2626.service.tables;

import me.grgamer2626.model.games.player.Hand;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.tables.PlayerSlots;
import me.grgamer2626.model.tables.TablesRepository;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.websocket.WebSocketService;
import me.grgamer2626.utils.scheduler.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TableManager implements TableService {
	
	private final TablesRepository tablesRepository;
	private final WebSocketService webSocketService;
	private final Scheduler scheduler;
	
	@Autowired
	public TableManager(TablesRepository tablesRepository, WebSocketService webSocketService, Scheduler scheduler) {
		this.tablesRepository = tablesRepository;
		this.webSocketService = webSocketService;
		this.scheduler = scheduler;
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public GameTable getTable(long id) {
		return tablesRepository.findById(id);
	}
	
	@Override
	public boolean takeSlot(long tableId, int slot, User user) {
		GameTable table = getTable(tableId);
		var playerSlots = table.getPlayerSlots();
		String userName = user.getName();
		
		if(!playerSlots.containsKey(slot) || playerSlots.isKeyTaken(slot) || playerSlots.occupiedAnyKey(userName)) return false;
		
		
		playerSlots.replace(slot, new Player(user.getId(), userName, slot));
		
		stopStartCountDown(table);
		String destination = "/topic/table/"+tableId+"/popupVisibility";
		webSocketService.sendTo(destination, "StartCountDownStop");
		
		if(playerSlots.getPlayerAmount() >= 2) {
			List<String> names = playerSlots.getNonNull().stream()
					.map(Player::getName)
					.collect(Collectors.toList());
			
			switchStartButtonVisibility(names, tableId, true);
		}
		return true;
	}
	
	@Override
	public boolean leaveSlot(long tableId, int slot) {
		GameTable table = getTable(tableId);
		var playerSlots = table.getPlayerSlots();
		
		if(!playerSlots.isKeyTaken(slot)) return false;
		
		if(!table.isGameStarted()) {
			String playerName = playerSlots.get(slot).getName();
			playerSlots.remove(slot);
			
			stopStartCountDown(table);
			String destination = "/topic/table/" + tableId + "/popupVisibility";
			webSocketService.sendTo(destination, "StartCountDownStop");
			
			if(playerSlots.getPlayerAmount() < 2) {
				List<String> names = playerSlots.getNonNull().stream()
						.map(Player::getName)
						.collect(Collectors.toList());
				names.add(playerName);
				
				switchStartButtonVisibility(names, tableId, false);
			}
			return true;
			
		}else {
			table.startWaitCountDown(scheduler);
			return false;
		}
	}
	
	@Override
	public void startGame(long tableId, String playerName) {
		GameTable table = getTable(tableId);
		var playerSlots = table.getPlayerSlots();
		
		Player player = playerSlots.getByName(playerName);
		
		if(player.isPushedStart()) return;
		
		player.setPushedStart(true);
		
		if(table.getStartingCountDownTaskId() == -1) {
			table.startStartingCountDown(webSocketService, this);
			String destination = "/topic/table/"+tableId+"/popupVisibility";
			webSocketService.sendTo(destination, "StartCountDownRun");
			return;
		}
		
		if(playerSlots.readyPlayersCount() == playerSlots.getPlayerAmount()) {
			stopStartCountDown(table);
			playerSlots.getNonNull().forEach(p-> p.setPushedStart(false));
			table.startGame();
			
			String destination = "/topic/table/"+tableId+"/popupVisibility";
			webSocketService.sendTo(destination, "StartGame");
		}
	}
	
	
	@Override
	public String endGame(long tableId, String playerName) {
		GameTable table = getTable(tableId);
		PlayerSlots playerSlots = table.getPlayerSlots();
		
		Player player = playerSlots.getByName(playerName);
		Hand hand = player.getOnHand();
		
		if(!hand.isEmpty() || !player.isLayDown()) return null;
		
		boolean anyIncorrectSequence = playerSlots.getNonNull().stream()
				.filter(Player::isLayDown)
				.map(Player::getSequences)
				.map(Map::values)
				.flatMap(Collection::stream)
				.anyMatch(sequence-> !sequence.isSequenceCorrect());
		
		if(anyIncorrectSequence) return null;
		
		table.endGame();
		
		return playerName;
	}
	
	private void switchStartButtonVisibility(List<String> playersNames, long tableId, boolean visible) {
		String destination = "/topic/table/" + tableId;
		if(visible) {
			destination +="/showStartButton";
			
		}else {
			destination += "/hideStartButton";
		}
		
		for(String playerName : playersNames) {
			webSocketService.sendToUser(playerName, destination, 1);
		}
	}
	
	private void stopStartCountDown(GameTable table) {
		if(table.getStartingCountDownTaskId() != -1) {
			table.stopStartingCountDown();
		}
	}
	
	
}
