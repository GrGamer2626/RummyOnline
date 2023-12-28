package me.grgamer2626.model.games;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.decks.Deck;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import me.grgamer2626.model.tables.PlayerSlots;
import me.grgamer2626.utils.Identifiable;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

public final class Game implements Identifiable<Long> {
	
	private final long id;
	private boolean paused = false;
	private final Deck deck;
	private final Deque<Card> stack;
	private final PlayerSlots playerSlots;
	private int currentTurnPlayer;
	
	public Game(long id, Deck deck, PlayerSlots playerSlots, int lastGameSlotStart) {
		this.id = id;
		this.deck = deck;
		this.stack = new LinkedList<>();
		
		this.playerSlots = playerSlots;
		
		for(Player player : playerSlots.getNonNull()) {
			player.getOnHand().clear();
			player.getSequences().values().forEach(Sequence::clear);
			player.setLayDown(false);
		}
		
		currentTurnPlayer = whoStartGame(lastGameSlotStart);
		dealCards();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public Long getId() {
		return id;
	}
	
	private void dealCards() {
		for(int i = 1 ; i <= 13; i++) {
			for(Player player : playerSlots.values()) {
				if(player == null) {
					continue;
				}
				player.takeCard(deck.poll());
			}
		}
	}
	
	private int whoStartGame(int lastGameSlotStart) {
		if(lastGameSlotStart == -1) {
			Random random = new Random();
			var list = playerSlots.getNonNull();
			Player player = playerSlots.getNonNull().get(random.nextInt(list.size()));
			
			return player.getSlot();
		}
		return next(lastGameSlotStart);
	}
	
	private int next(int currentTurnPlayer) {
		var list = playerSlots.getNonNull();
		
		Player player = playerSlots.get(currentTurnPlayer);
		int nextIndex = (list.indexOf(player) + 1) % list.size();
		
		return this.playerSlots.getKey(list.get(nextIndex));
	}
	
	
	public int next() {
		currentTurnPlayer = next(currentTurnPlayer);
		return currentTurnPlayer;
	}
	
	public int getCurrentTurnPlayer() {
		return currentTurnPlayer;
	}
	
	public Card takeFromDeck() {
		return deck.poll();
	}
	
	public Deque<Card> getStack() {
		return stack;
	}
	
	public Card pullFromStack() {
		return stack.pollLast();
	}
	
	public Card peekFromStack() {
		return stack.peekLast();
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public PlayerSlots getPlayerSlots() {
		return playerSlots;
	}
	
	public Player getPlayer(int slot) {
		return playerSlots.get(slot);
	}
	
	public void throwCard(Card card) {
		stack.add(card);
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
}
