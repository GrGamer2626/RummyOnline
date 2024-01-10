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
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@MessageMapping("/rummy/{tableId}/startGame")
	@SendTo("/topic/startGame")
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
		
		String destination = "/topic/pm/startGame";
		webSocketService.sendToUser(player.getName(), destination, onHand);
		
		return onHand;
	}
	
	public boolean yourTurn(long tableId, int slot) {
		Game game = gameService.getGame(tableId);
		Player player = game.getPlayer(slot);
		
		player.setPhase(TurnPhases.TAKE_CARD);
		
		boolean layDown = player.isLayDown();
		
		String destination = "/topic/pm/yourTurn";
		webSocketService.sendToUser(player.getName(), destination, layDown);
		
		return layDown;
	}
	
	//*********************** Take From Deck ***********************//
	@MessageMapping("/rummy/{tableId}/takeFromDeck")
	@SendTo("/topic/takeFromDeck")
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
		
		String destination = "/topic/pm/takeFromDeck";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	@MessageMapping("/rummy/{tableId}/takeFromStack")
	@SendTo("/topic/takeFromStack")
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
		
		String destination = "/topic/pm/takeFromStack";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	
	@MessageMapping("/rummy/{tableId}/confirmTakenCard")
	@SendToUser("/topic/pm/confirmTakenCard")
	public CardDto confirmTakenCard(@DestinationVariable long tableId, @Payload int currentSlot, Principal principal) {
		String playerName = principal.getName();
		
		CardDto dto = gameService.confirmTakenCard(tableId, playerName);
		if(dto == null) return null;
		
		Player player = gameService.getGame(tableId).getPlayerSlots().getByName(playerName);
		player.setPhase(TurnPhases.MOVE_CARDS);
		
		return dto;
	}
	
	@MessageMapping("/rummy/{tableId}/returnCard")
	@SendTo("/topic/returnCard")
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
		
		String destination = "/topic/pm/returnCard";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	
	//*********************** Move Card ***********************//
	@MessageMapping("/rummy/{tableId}/moveCard")
	@SendTo("/topic/moveCard")
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
		String destination = "/topic/pm/moveCard";
		webSocketService.sendToUser(player.getName(), destination, dto);
		
		return dto;
	}
	
	//*********************** Replace Joker ***********************//
	@MessageMapping("/rummy/{tableId}/replaceJoker")
	@SendTo("/topic/replaceJoker")
	@SendToUser("/topic/pm/replaceJoker")
	public ReplaceJokerDto replaceJoker(@DestinationVariable long tableId, @Payload MoveCardInputDto moveCardDto, Principal principal) {
		Game game = gameService.getGame(tableId);
		String playerName = principal.getName();
		
		return gameService.replaceJoker(tableId, playerName, moveCardDto);
	}
	
	//*********************** Throw Card ***********************//
	@MessageMapping("/rummy/{tableId}/throwCard")
	@SendTo("/topic/throwCard")
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
		
		String destination = "/topic/pm/throwCard";
		webSocketService.sendToUser(player.getName(), destination, throwCardDto);
		
		return throwCardDto;
	}
	
	//*********************** Lay Down ***********************//
	@MessageMapping("/rummy/{tableId}/layDown")
	@SendTo("/topic/layDown")
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
		String destination = "/topic/pm/layDown";
		webSocketService.sendToUser(player.getName(), destination, map);
		
		return map;
	}
	
}
