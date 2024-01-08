package me.grgamer2626.model.tables.utils;

import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.tables.PlayerSlots;
import me.grgamer2626.service.tables.TableService;
import me.grgamer2626.service.websocket.WebSocketService;
import me.grgamer2626.utils.dto.game.SlotDto;

public class StartCountDown implements Runnable {
	
	private final WebSocketService webSocketService;
	private final TableService tableService;
	private final long tableId;
	private final String direction;
	private final String leaveDirection;
	private long duration;
	
	public StartCountDown(long tableId, long duration, WebSocketService webSocketService, TableService tableService) {
		this.tableId = tableId;
		this.duration = (duration / 1000);
		
		this.webSocketService = webSocketService;
		this.tableService = tableService;
		
		this.direction="/topic/table/"+tableId+"/startGameCountDown";
		this.leaveDirection ="/topic/table/"+tableId+"/leaveSlot";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void run() {
		webSocketService.sendTo(direction, duration);
		duration--;
		
		if(duration >= 0) return;
		
		GameTable table = tableService.getTable(tableId);
		
		PlayerSlots playerSlots = table.getPlayerSlots();
		kick(playerSlots);
		resetPressStart(playerSlots);
		
		table.setStartingCountDownTaskId(-1);
	}
	
	private void kick(PlayerSlots playerSlots) {
		playerSlots.getNonNull().stream()
				.filter(player -> !player.isPushedStart())
				.forEach(player -> {
					int slot = playerSlots.getKey(player);
					tableService.leaveSlot(tableId, slot);
					webSocketService.sendTo(leaveDirection, new SlotDto(slot, player.getName()));
				});
	}
	
	private void resetPressStart(PlayerSlots playerSlots) {
		playerSlots.getNonNull()
				.forEach(player -> player.setPushedStart(false));
	}
}
