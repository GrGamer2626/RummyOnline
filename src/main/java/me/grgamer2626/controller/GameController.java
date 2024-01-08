package me.grgamer2626.controller;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.TurnPhases;
import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.tables.PlayerSlots;
import me.grgamer2626.service.games.GameService;
import me.grgamer2626.service.websocket.WebSocketService;
import me.grgamer2626.utils.dto.game.CardDto;
import me.grgamer2626.utils.dto.game.LayDownDto;
import me.grgamer2626.utils.dto.game.ReturnCardDto;
import me.grgamer2626.utils.dto.game.moveCard.MoveCardDto;
import me.grgamer2626.utils.dto.game.moveCard.MoveCardInputDto;
import me.grgamer2626.utils.dto.game.moveCard.ReplaceJokerDto;
import me.grgamer2626.utils.dto.game.takeCard.TakeCardDto;
import me.grgamer2626.utils.dto.game.takeCard.TakeFromStackDto;
import me.grgamer2626.utils.dto.game.throwCardDto.ThrowCardDto;
import me.grgamer2626.utils.dto.game.throwCardDto.ThrowCardInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GameController {
	
	private final GameService gameService;
	private final WebSocketService webSocketService;
	
	
	@Autowired
	public GameController(GameService gameService, WebSocketService webSocketService) {
		this.gameService = gameService;
		this.webSocketService = webSocketService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@MessageMapping("/rummy/table/{tableId}/startGame")
	@SendTo("/topic/table/{tableId}/startGame")
	public Map<Integer, Integer> startGame(@DestinationVariable long tableId, @Payload long currentSlot) {
		Map<Integer, Integer> cardsCount = gameService.startGame(tableId);
		
		Game game = gameService.getGame(tableId);
		PlayerSlots playerSlots = game.getPlayerSlots();
		
		for(Player player : playerSlots.getNonNull()) {
			startGame(tableId, player.getSlot(), player);
		}
		yourTurn(tableId, game.getCurrentTurnPlayer());
		
		return cardsCount;
	}
	
	public List<CardDto> startGame(long tableId, int slot, Player player) {
		
		List<CardDto> onHand = player.getOnHand().stream()
										.map(card -> new CardDto(card.getId(), card.getImgPath()))
										.collect(Collectors.toList());
		
		String destination = "/topic/table/" + tableId + "/slot/" + slot + "/startGame";
		webSocketService.sendToUser(player.getName(), destination, onHand);
		
		return onHand;
	}
	
	public boolean yourTurn(long tableId, int slot) {
		Game game = gameService.getGame(tableId);
		Player player = game.getPlayer(slot);
		
		player.setPhase(TurnPhases.TAKE_CARD);
		
		boolean layDown = player.isLayDown();
		
		String destination = "/topic/table/" + tableId + "/slot/" + slot + "/yourTurn";
		webSocketService.sendToUser(player.getName(), destination, layDown);
		
		return layDown;
	}
	
	//*********************** Take From Deck ***********************//
	@MessageMapping("/rummy/table/{tableId}/takeFromDeck")
	@SendTo("/topic/table/{tableId}/takeFromDeck")
	public Integer takeFromDeck(@DestinationVariable long tableId, int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		Card card = gameService.takeFromDeck(tableId, playerName);
		
		if(card == null) return null;
		
		int slot = gameService.getGame(tableId).getPlayerSlots().getByName(playerName).getSlot();
		
		takeFromDeck(tableId, slot, card);
		
		return slot;
	}
	
	private TakeCardDto takeFromDeck(long tableId, int playerSlot, Card card) {
		TakeCardDto dto = gameService.createTakeCardDto(tableId, playerSlot, card);
		
		Player player =  gameService.getGame(tableId).getPlayer(playerSlot);
		player.setPhase(TurnPhases.MOVE_CARDS);
		
		String destination = "/topic/table/" + tableId + "/slot/" + playerSlot + "/takeFromDeck";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	@MessageMapping("/rummy/table/{tableId}/takeFromStack")
	@SendTo("/topic/table/{tableId}/takeFromStack")
	private TakeFromStackDto takeFromStack(@DestinationVariable long tableId, int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		Card takenCard = gameService.takeFromStack(tableId, playerName);
		if(takenCard == null) return null;
		
		Game game = gameService.getGame(tableId);
		int slot = game.getPlayerSlots().getByName(playerName).getSlot();
		
		takeFromStack(tableId, slot, takenCard);
		
		Card cardBefore = game.peekFromStack();
		
		return new TakeFromStackDto(slot, new CardDto(cardBefore.getId(), cardBefore.getImgPath()));
	}
	
	
	private TakeCardDto takeFromStack(long tableId, int playerSlot, Card takenCard) {
		TakeCardDto dto = gameService.createTakeCardDto(tableId, playerSlot, takenCard);
		
		Player player =  gameService.getGame(tableId).getPlayer(playerSlot);
		
		player.setCardIdTakenFromStack(takenCard.getId());
		player.setPhase(TurnPhases.CARD_TAKEN_FROM_STACK);
		
		String destination = "/topic/table/" + tableId + "/slot/" + playerSlot + "/takeFromStack";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	
	@MessageMapping("/rummy/table/{tableId}/confirmTakenCard")
//	@SendToUser("/topic/table/{tableId}/slot/{playerSlot}/confirmTakenCard")
	public CardDto confirmTakenCard(@DestinationVariable long tableId, @Payload int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		CardDto dto = gameService.confirmTakenCard(tableId, playerName);
		if(dto == null) return null;
		
		Player player = gameService.getGame(tableId).getPlayerSlots().getByName(playerName);
		player.setPhase(TurnPhases.MOVE_CARDS);
		int playerSlot = player.getSlot();
		
		
		String destination = "/topic/table/" + tableId + "/slot/" + playerSlot + "/confirmTakenCard";
		webSocketService.sendToUser(playerName, destination, dto);
		
		return dto;
	}
	
	@MessageMapping("/rummy/table/{tableId}/returnCard")
	@SendTo("/topic/table/{tableId}/returnCard")
	public ReturnCardDto returnCartFromStack(@DestinationVariable long tableId, @Payload int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		ReturnCardDto dto =  gameService.returnCardFromStack(tableId, playerName);
		if(dto == null) return null;
		
		Game game = gameService.getGame(tableId);
		Player player = game.getPlayerSlots().getByName(playerName);
		
		
		Card card = game.takeFromDeck();
		player.takeCard(card);
		
		returnCardFromStack(tableId, player.getSlot(), card);
		
		
		return dto;
	}
	
	
	public TakeCardDto returnCardFromStack(long tableId, int playerSlot, Card card) {
		TakeCardDto dto = gameService.createTakeCardDto(tableId, playerSlot, card);
		
		Player player =  gameService.getGame(tableId).getPlayer(playerSlot);
		player.setPhase(TurnPhases.MOVE_CARDS);
		
		String destination = "/topic/table/" + tableId + "/slot/" + playerSlot + "/returnCard";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	
	//*********************** Move Card ***********************//
	@MessageMapping("/rummy/table/{tableId}/moveCard")
	@SendTo("/topic/table/{tableId}/moveCard")
	public MoveCardDto moveCard(@DestinationVariable long tableId, @Payload MoveCardInputDto moveCardDto, Principal principal) {
		String playerName = principal.getName();
		
		MoveCardDto dto = gameService.moveCard(tableId, playerName, moveCardDto);
		
		if(dto == null) return null;
		
		Game game = gameService.getGame(tableId);
		int playerSlot = game.getPlayerSlots().getByName(playerName).getSlot();
		
		moveCard(tableId, playerSlot, moveCardDto);
		
		return dto;
	}
	
	private MoveCardDto moveCard(long tableId, int slot, @Payload MoveCardInputDto moveCardDto) {
		MoveCardDto dto = gameService.moveCard(tableId, moveCardDto);
		
		Player player =  gameService.getGame(tableId).getPlayer(slot);
		String destination = "/topic/table/" + tableId + "/slot/" + slot + "/moveCard";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	//*********************** Replace Joker ***********************//
	@MessageMapping("/rummy/table/{tableId}/replaceJoker")
	@SendTo("/topic/table/{tableId}/replaceJoker")
	public ReplaceJokerDto replaceJoker(@DestinationVariable long tableId, @Payload MoveCardInputDto moveCardDto, Principal principal) {
		String playerName = principal.getName();
		
		ReplaceJokerDto dto = gameService.replaceJoker(tableId, playerName, moveCardDto);
		if(dto == null) return null;
		
		Game game = gameService.getGame(tableId);
		int playerSlot = game.getPlayerSlots().getByName(playerName).getSlot();
		
		replaceJoker(tableId, playerSlot, dto);
		
		return dto;
	}
	
	public void replaceJoker(long tableId, int slot, ReplaceJokerDto replaceJokerDto) {
		Player player =  gameService.getGame(tableId).getPlayer(slot);
		String destination = "/topic/table/" + tableId + "/slot/" + slot + "/replaceJoker";
		webSocketService.sendToUser(player.getName(), destination, replaceJokerDto);
	}
	
	//*********************** Throw Card ***********************//
	@MessageMapping("/rummy/table/{tableId}/throwCard")
	@SendTo("/topic/table/{tableId}/throwCard")
	public ThrowCardDto throwCard(@DestinationVariable long tableId, @Payload ThrowCardInputDto throwCardDto, Principal principal) {
		String playerName = principal.getName();
		
		ThrowCardDto dto = gameService.throwCard(tableId, playerName, throwCardDto);
		if(dto == null) return null;
		
		Game game = gameService.getGame(tableId);
		Player player = game.getPlayerSlots().getByName(playerName);
		int playerSlot = player.getSlot();
		
		throwCard(tableId, playerSlot, throwCardDto);
		
		player.setPhase(TurnPhases.NOT_YOUR_TURN);
		
		int nextTurn = game.next();
		yourTurn(tableId, nextTurn);
		
		return dto;
	}

	private ThrowCardInputDto throwCard(long tableId,  int slot, ThrowCardInputDto throwCardDto) {
		Player player = gameService.getGame(tableId).getPlayer(slot);
		
		String destination = "/topic/table/" + tableId + "/slot/" + slot + "/throwCard";
		webSocketService.sendToUser(player.getName(), destination, throwCardDto);
		
		return throwCardDto;
	}
	
	//*********************** Lay Down ***********************//
	@MessageMapping("/rummy/table/{tableId}/layDown")
	@SendTo("/topic/table/{tableId}/layDown")
	public LayDownDto layDown(@DestinationVariable long tableId, @Payload int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		LayDownDto dto = gameService.layDown(tableId, playerName);
		if(dto == null) return null;
		
		Game game = gameService.getGame(tableId);
		int slot = game.getPlayerSlots().getByName(playerName).getSlot();
		
		layDown(tableId, slot);
		
		return dto;
	}

	public Map<Integer, Boolean> layDown(long tableId, int slot) {
		Map<Integer, Boolean> map = gameService.layDown(tableId);
		
		Player player =  gameService.getGame(tableId).getPlayer(slot);
		String destination = "/topic/table/" + tableId + "/slot/" + slot + "/layDown";
		webSocketService.sendToUser(player.getName(), destination, map);
		
		return map;
	}
	
}
