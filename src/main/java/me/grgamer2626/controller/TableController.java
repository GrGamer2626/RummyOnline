package me.grgamer2626.controller;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.tables.PlayerSlots;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.UserRepository;
import me.grgamer2626.service.tables.TableService;
import me.grgamer2626.service.websocket.WebSocketService;
import me.grgamer2626.utils.dto.game.EndGameDto;
import me.grgamer2626.utils.dto.game.SlotDto;
import me.grgamer2626.utils.scheduler.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Deque;

@Controller
public class TableController {
	private final TableService tableService;
	private final UserRepository userRepository;
	private final WebSocketService webSocketService;
	private final Scheduler scheduler;
	
	@Autowired
	public TableController(TableService tableService, UserRepository userRepository, WebSocketService webSocketService, Scheduler scheduler) {
		this.tableService = tableService;
		this.userRepository = userRepository;
		this.webSocketService = webSocketService;
		this.scheduler = scheduler;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/table/{tableId}")
	public String table(@PathVariable long tableId, Model model) {
		GameTable table = tableService.getTable(tableId);
		
		if (table == null) return "lobby";
		
		model.addAttribute("tableId", table.getId());
		model.addAttribute("tableOwner", table.getTableOwner());
		model.addAttribute("isGameStarted", table.isGameStarted());
		
		Card lastThrown = null;
		if(table.isGameStarted()) {
			Game game = table.getGame();
			Deque<Card> stack = game.getStack();
			lastThrown = stack.peekLast();
		}
		model.addAttribute("thrownCard", lastThrown);
		
		for(int slot = 1 ; slot <= 6 ; slot++) {
			String attribute = "player" + slot;
			model.addAttribute(attribute, table.getPlayerSlots().get(slot));
		}
		return "table";
	}
	
	@MessageMapping("/rummy/table/{tableId}/takeSlot")
	@SendTo("/topic/table/{tableId}/takeSlot")
	public SlotDto takeSlot(@DestinationVariable long tableId, int slotNumber, Principal principal) {
		String playerName = principal.getName();
		User user = userRepository.findByName(principal.getName());
		
		boolean success = tableService.takeSlot(tableId, slotNumber, user);
		
		SlotDto slotDto = null;
		
		if(success) {
			slotDto = new SlotDto(slotNumber, playerName);
			String destination = "/topic/table/"+tableId+"/slotSubscription";
			webSocketService.sendToUser(playerName, destination, slotDto);
		}
		return slotDto;
	}
	
	@MessageMapping("/rummy/table/{tableId}/leaveSlot")
	@SendTo("/topic/table/{tableId}/leaveSlot")
	public SlotDto leaveSlot(@DestinationVariable long tableId, int slotNumber, Principal principal) {
		boolean success = tableService.leaveSlot(tableId, slotNumber);
		
		SlotDto slotDto = null;
		if(success) {
			slotDto = new SlotDto(slotNumber, principal.getName());
		}
		return slotDto;
	}
	
	@MessageMapping("/rummy/table/{tableId}/startGameCountDown")
	public void startingCountDown(@DestinationVariable long tableId, int slotNumber, Principal principal) {
		String playerName = principal.getName();
		tableService.startGame(tableId, playerName);
	}
	
	@MessageMapping("/rummy/table/{tableId}/endGame")
	@SendTo("/topic/table/{tableId}/endGame")
	public EndGameDto endGame(@DestinationVariable long tableId, @Payload int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		String winnerName = tableService.endGame(tableId, playerName);
		if(winnerName == null) return null;
		
		PlayerSlots playerSlots = tableService.getTable(tableId).getPlayerSlots();
		for(Player player: playerSlots.getNonNull()) {
			endGame(tableId, player.getSlot(), player.getName());
		}
		
		return new EndGameDto(winnerName);
	}
	
	private void endGame(long tableId, int playerSlot, String playerName) {
		String destination = "topic/table/" + tableId + "/slot/" + playerSlot + "/endGame";
		webSocketService.sendToUser(playerName, destination, playerSlot);
	}
}
