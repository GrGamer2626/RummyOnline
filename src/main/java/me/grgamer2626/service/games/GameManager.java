package me.grgamer2626.service.games;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.TurnPhases;
import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.decks.Deck;
import me.grgamer2626.model.games.player.Hand;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import me.grgamer2626.model.tables.PlayerSlots;
import me.grgamer2626.service.tables.TableService;
import me.grgamer2626.service.websocket.WebSocketService;
import me.grgamer2626.utils.GameCollection;
import me.grgamer2626.utils.dto.CardDto;
import me.grgamer2626.utils.dto.LayDownDto;
import me.grgamer2626.utils.dto.ReturnCardDto;
import me.grgamer2626.utils.dto.moveCard.*;
import me.grgamer2626.utils.dto.takeCard.TakeCardDto;
import me.grgamer2626.utils.dto.throwCardDto.ThrowCardDto;
import me.grgamer2626.utils.dto.throwCardDto.ThrowCardInputDto;
import me.grgamer2626.utils.dto.throwCardDto.ThrowCardOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameManager implements GameService {
	
	private final TableService tableService;
	private final WebSocketService webSocketService;
	
	@Autowired
	public GameManager(TableService tableService, WebSocketService webSocketService) {
		this.tableService = tableService;
		this.webSocketService = webSocketService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public Game getGame(long tableId) {
		return tableService.getTable(tableId).getGame();
	}
	
	@Override
	public Map<Integer, Integer> startGame(long tableId) {
		Game game = getGame(tableId);
		Map<Integer, Integer> cardsCount = new ConcurrentHashMap<>();
		PlayerSlots playerSlots = game.getPlayerSlots();
		
		for(int slot : playerSlots.keySet()) {
			Player player = playerSlots.get(slot);
			int cards = 0;
			
			if (player != null) cards = player.handLength();
			
			cardsCount.put(slot, cards);
		}
		return cardsCount;
	}
	
	
	@Override
	public Card takeFromDeck(long tableId, String playerName) {
		Game game = getGame(tableId);
		
		resetDeck(tableId);
		
		PlayerSlots playerSlots = game.getPlayerSlots();
		Player player = playerSlots.getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.TAKE_CARD) return null;
		
		Card card = game.takeFromDeck();
		player.takeCard(card);
		
		return card;
	}
	
	@Override
	public Card takeFromStack(long tableId, String playerName) {
		Game game = getGame(tableId);
		
		PlayerSlots playerSlots = game.getPlayerSlots();
		Player player = playerSlots.getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.TAKE_CARD) return null;
		
		Card card = game.pullFromStack();
		player.takeCard(card);
		
		return card;
	}
	
	@Override
	public TakeCardDto createTakeCardDto(long tableId, int slot, Card card) {
		Game game = getGame(tableId);
		PlayerSlots playerSlots = game.getPlayerSlots();
		
		CardDto cardDto = new CardDto(card.getId(), card.getImgPath());
		int position = playerSlots.get(slot).getOnHand().indexOf(card);
		Map<Integer, Boolean> layDownPlayers = getLayDownPlayers(playerSlots);
		
		return new TakeCardDto(cardDto, position, layDownPlayers);
	}
	
	
	@Override
	public CardDto confirmTakenCard(long tableId, String playerName) {
		Game game = getGame(tableId);
		PlayerSlots playerSlots = game.getPlayerSlots();
		
		Player player = playerSlots.getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.CARD_TAKEN_FROM_STACK) return null;
		
		int cardId = player.getCardIdTakenFromStack();
		
		boolean containsCard = false;
		Card card = null;
		
		for(Player otherPlayer : playerSlots.getNonNull()) {
			if(!otherPlayer.isLayDown()) continue;
			
			for(Sequence sequence : otherPlayer.getSequences().values()) {
				if(!sequence.isSequenceCorrect()) return null;
				
				if(sequence.containId(cardId)) {
					containsCard = true;
					card = sequence.getById(cardId);
				}
			}
		}
		if(!containsCard) return null;
		
		card.setMovable(false);
		
		return new CardDto(card.getId(), card.getImgPath());
	}
	
	@Override
	public ReturnCardDto returnCardFromStack(long tableId, String playerName) {
		Game game = getGame(tableId);
		
		resetDeck(tableId);
		
		PlayerSlots playerSlots = game.getPlayerSlots();
		
		Player player = playerSlots.getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.CARD_TAKEN_FROM_STACK) return null;
		
		int cardId = player.getCardIdTakenFromStack();
		Card cardFromStack = null;
		GameCollection collection = null;
		
		for(Player otherPlayer : playerSlots.getNonNull()) {
			if(!otherPlayer.isLayDown()) continue;
			
			if(player.equals(otherPlayer)) {
				Hand hand = otherPlayer.getOnHand();
				if(hand.containId(cardId)) {
					cardFromStack = hand.getById(cardId);
					collection = hand;
					break;
				}
			}
			
			for(Sequence sequence : otherPlayer.getSequences().values()) {
				if(sequence.containId(cardId)) {
					cardFromStack = sequence.getById(cardId);
					collection = sequence;
					break;
				}
			}
		}
		if (collection == null || cardFromStack == null) return null;
		
		collection.moveTo(cardId, game.getStack());
		
		CardDto cardDto = new CardDto(cardFromStack.getId(), cardFromStack.getImgPath());
		return new ReturnCardDto(playerSlot, cardDto, collection.getNumber(), collection.getSlot());
	}
	
	private void resetDeck(long tableId) {
		Game game = getGame(tableId);
		
		Deck deck = game.getDeck();
		
		if(deck.isEmpty()) {
			Deque<Card> stack = game.getStack();
			Card lastAdded = stack.pollLast();
			
			deck.addAll(game.getStack());
			deck.shuffle();
			
			stack.clear();
			stack.add(lastAdded);
		}
 	}
	
	@Override
	public MoveCardDto moveCard(long tableId, String playerName, MoveCardInputDto moveCardDto) {
		Game game = getGame(tableId);
		
		Player player = game.getPlayerSlots().getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || (player.getPhase() != TurnPhases.MOVE_CARDS && player.getPhase() != TurnPhases.CARD_TAKEN_FROM_STACK)) return null;

		int cardId = moveCardDto.getCardId();
		Figures figure = moveCardDto.getFigure();
		
		GameCollection source = moveCardDto.getSource(game);
		GameCollection destination = moveCardDto.getDestination(game);
		
		
		if(isMoveIllegal(cardId, player, source, destination, figure)) return null;
		
		
		Card card = tryMoveCard(cardId, figure, source, destination);
		if(card == null) return null;
		
		
		if(player.isLayDown()) {
			int position = destination.indexOf(card);
			CardDto cardDto = new CardDto(card.getId(), card.getImgPath());
			
			return new MoveCardLayDownDto(cardDto, position, moveCardDto);
		}
		
		return new MoveCardDto(playerSlot, moveCardDto.getSourceSlot(), moveCardDto.getSourceNumber(), moveCardDto.getDestinationSlot(), moveCardDto.getDestinationNumber());
	}
	
	@Override
	public MoveCardDto moveCard(long tableId, MoveCardInputDto moveCardDto) {
		Game game = getGame(tableId);
		
		GameCollection destination = moveCardDto.getDestination(game);
		int position = destination.indexOf(destination.getById(moveCardDto.getCardId()));
		
		return new MoveCardOutputDto(position, moveCardDto);
	}
	
	@Override
	public ReplaceJokerDto replaceJoker(long tableId, String playerName, MoveCardInputDto moveCardDto) {
		Game game = getGame(tableId);
		
		Player player = game.getPlayerSlots().getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.MOVE_CARDS) return null;
		
		int cardId = moveCardDto.getCardId();
		
		GameCollection source = moveCardDto.getSource(game);
		GameCollection destination = moveCardDto.getDestination(game);
		
		if(isMoveIllegal(cardId, player, source, destination)) return null;
		
		Card card = source.getById(cardId);
		
		if(destination.isSequence() && destination.containsJoker() && destination.containsDetails(card.getFigure(), card.getColor())) {
			Joker joker = replaceJoker(card, player, source, destination);
			if(joker == null) return null;
			
			CardDto cardDto = new CardDto(card.getId(), card.getImgPath());
			
			return new ReplaceJokerDto(playerSlot, moveCardDto.getSourceSlot(), moveCardDto.getSourceNumber(), cardDto, joker.getId());
		}
		
		return null;
	}
	
	private Joker replaceJoker(Card card, Player player, GameCollection source, GameCollection destination) {
		Figures cardFigure = card.getFigure();
		Colors cardColor =  card.getColor();
		
		Sequence sequence = (Sequence) destination;
		Card similar = sequence.getByDetails(cardFigure, cardColor);
		
		if(!similar.isJoker()) return null;
		
		Joker joker = (Joker) similar;
		joker.setMovable(true);
		
		sequence.moveTo(joker.getId(), null, player.getOnHand());
		source.moveTo(card.getId(), destination);
		
		return joker;
	}
	
	
	
	
	@Override
	public ThrowCardDto throwCard(long tableId, String playerName, ThrowCardInputDto throwCardDto) {
		Game game = getGame(tableId);
		PlayerSlots playerSlots = game.getPlayerSlots();
		
		Player player = playerSlots.getByName(playerName);
		int playerSlot = playerSlots.getKey(player);
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.MOVE_CARDS) return null;
		
		GameCollection source = throwCardDto.getSource(game);
		Card card = source.moveTo(throwCardDto.getCardId(), game.getStack());
		
		if(player.isLayDown()) {
			if(!areSequencesCorrect(playerSlots)) {
				card = game.pullFromStack();
				source.add(card);
				return null;
			}
			makeUnmovable(playerSlots);
		}
		
		if(card instanceof Joker joker) joker.setFigure(null);
		
		CardDto cardDto = new CardDto(card.getId(), card.getImgPath());
		
		return new ThrowCardOutputDto(cardDto, playerSlot, player.isLayDown(), throwCardDto.getSourceSlot(), throwCardDto.getSourceNumber());
	}
	
	@Override
	public LayDownDto layDown(long tableId, String playerName) {
		Game game = getGame(tableId);
		
		Player player = game.getPlayerSlots().getByName(playerName);
		int playerSlot = player.getSlot();
		
		if(playerSlot != game.getCurrentTurnPlayer() || player.getPhase() != TurnPhases.MOVE_CARDS) return null;
		
		boolean layDown = player.layDown();
		
		if(layDown) {
			player.getSequences().values().forEach(sequence ->
					sequence.forEach(card->
							card.setMovable(false)));
			
			Map<Integer, List<CardDto>> sequences = player.getSequences().entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey,
							entry-> entry.getValue().stream()
									.map(card-> new CardDto(card.getId(), card.getImgPath()))
									.toList()));
			
			return new LayDownDto(playerSlot, sequences);
		}
		return null;
	}
	
	@Override
	public Map<Integer, Boolean> layDown(long tableId) {
		Game game = getGame(tableId);
		
		return getLayDownPlayers(game.getPlayerSlots());
	}
	
	
	
	
	
	
	
	private boolean isMoveIllegal(int cardId, Player player, GameCollection source, GameCollection destination, Figures figure) {
		return isMoveIllegal(cardId,player,source,destination) || isJokerMoveIllegal(cardId, source, destination, figure);
	}
	
	private boolean isMoveIllegal(int cardId, Player player, GameCollection source, GameCollection destination) {
		//When none move was made
		if(source.equals(destination)) return true;
		
		//If source does not contain that card;
		if(!source.containId(cardId)) return true;
		
		if(destination.getOwner().getSlot() != player.getSlot()) {
			//When try to put card in someone others hand
			if(destination.isHand()) return true;
			
			//When try to add card to someone others sequence, and player isn't lay down yet
			if(!player.isLayDown()) return true;
			
			//When try to add card to someone others sequence, and destination player isn't lay down yet
			if(!destination.getOwner().isLayDown()) return true;
		}
		
		//When try to move card from someone others hand
		if(source.getOwner().getSlot() != player.getSlot() && source.isHand()) return true;
		
		//When card is set as unmovable
		if(!source.getById(cardId).isMovable()) return true;
		
		return false;
	}
	
	private boolean isJokerMoveIllegal(int cardId, GameCollection source, GameCollection destination, Figures figure) {
		//When card is Joker and its figure was not presided
		Card card = source.getById(cardId);
		return card.isJoker() && destination.isSequence() && figure == null;
	}
	
	
	private Map<Integer, Boolean> getLayDownPlayers(PlayerSlots playerSlots) {
		Map<Integer, Boolean> layDownPlayers = new HashMap<>();
		
		for(int playerSlot : playerSlots.keySet()) {
			Player player = playerSlots.get(playerSlot);
			
			if(player != null) {
				layDownPlayers.put(playerSlot, player.isLayDown());
				continue;
			}
			layDownPlayers.put(playerSlot, false);
		}
		
		return layDownPlayers;
	}
	
	private Card tryMoveCard(int cardId, Figures figure, GameCollection source, GameCollection destination) {
		Card card = source.getById(cardId);
		
		if(card.isJoker()) {
			card = moveJoker(card, source, destination, figure);
			return card;
		}
		
		if(destination.isSequence() && destination.containsJoker() && destination.containsDetails(card.getFigure(), card.getColor())) return null;
		
		card = moveNormalCard(cardId, source, destination);
		return card;
	}
	
	private Joker moveJoker(Card card, GameCollection source, GameCollection destination, Figures figure) {
		Joker joker = (Joker) card;
		
		int jokerId = joker.getId();
		Figures previousFigure = joker.getFigure();
		
		if(!destination.isSequence()) {
			joker = source.moveTo(jokerId, null, destination);
			return joker;
		}
		joker = source.moveTo(jokerId, figure, destination);
		
		Sequence sequence = (Sequence) destination;
		
		if(!sequence.isSequenceCorrect() && sequence.size() > 2) {
			destination.moveTo(jokerId, previousFigure, source);
			return null;
		}
		return joker;
	}
	
	private Card moveNormalCard(int cardId, GameCollection source, GameCollection destination) {
		Card card = source.moveTo(cardId, destination);
		
		if(!destination.isSequence()) return card;
		
		Sequence sequence = (Sequence) destination;
		
		if(!sequence.isSequenceCorrect() && sequence.size() > 2) {
			destination.moveTo(cardId, source);
			return null;
		}
		
		return card;
	}
	
	private boolean areSequencesCorrect(PlayerSlots playerSlots) {
		for(Player otherPlayer : playerSlots.getNonNull()) {
			if(!otherPlayer.isLayDown()) continue;
			
			for(Sequence sequence : otherPlayer.getSequences().values()) {
				if(!sequence.isSequenceCorrect()) return false;
			}
		}
		return true;
	}
	
	private void makeUnmovable(PlayerSlots playerSlots) {
		for(Player otherPlayer : playerSlots.getNonNull()) {
			if(!otherPlayer.isLayDown()) continue;
			
			for(Sequence sequence : otherPlayer.getSequences().values()) {
				sequence.forEach(card ->card.setMovable(false));
			}
		}
	}
}
