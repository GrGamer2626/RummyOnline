package me.grgamer2626.model.games.player;

import me.grgamer2626.model.games.TurnPhases;
import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.player.sequences.Sequence;

import java.util.HashMap;
import java.util.Map;

public class Player {
	
	//Authentication
	private final long id;
	private final String name;
	private final int slot;
	
	//Game attributes
	private final Hand onHand;
	private final Map<Integer, Sequence> sequences;
	private boolean layDown;
	
	//Utils
	private TurnPhases phase;
	private int cardId;
	private boolean pushedStart;
	
	
	public Player(long id, String name, int slot) {
		this.id = id;
		this.name = name;
		this.slot = slot;
		
		onHand = new Hand(this);
		sequences = new HashMap<>();
		for (int i = 1; i <= 4; i++) {
			sequences.put(i, new Sequence(i, this));
		}
		
		layDown = false;
		pushedStart = false;
		phase = TurnPhases.NOT_YOUR_TURN;
		cardId = -1;
	}
	
	/**
	 * Returns id number of the player. The id of the player is identical with id of the user.
	 *
	 * @return id of the player
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Returns nickname of the player. The nickname of the player is identical with nickname of the user.
	 *
	 * @return nickname of the player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the slot number taken by this player.
	 *
	 * @return slot number
	 */
	public int getSlot() {
		return slot;
	}
	//*********** Game Attributes ***********//
	
	public Hand getOnHand() {
		return onHand;
	}
	
	public int handLength() {
		return onHand.size();
	}
	
	public void takeCard(Card card) {
		onHand.add(card);
	}
	
	public Map<Integer, Sequence> getSequences() {
		return sequences;
	}
	
	public Sequence getSequence(int number) {
		return sequences.get(number);
	}
	
	public boolean isLayDown() {
		return layDown;
	}
	
	public void setLayDown(boolean layDown) {
		this.layDown = layDown;
	}
	
	public boolean layDown() {
		int total = 0;
		boolean anyClearSequence = false;
		
		for (Sequence sequence : sequences.values()) {
			int sequenceVal = sequence.getSequenceValue();
			total += sequenceVal;
			
			if (!sequence.isSequenceCorrect()) {
				return false;
			}
			if (sequence.isClear()) {
				anyClearSequence = true;
			}
		}
		layDown = anyClearSequence && (total > 50);
		
		return layDown;
	}
	
	public TurnPhases getPhase() {
		return phase;
	}
	
	public void setPhase(TurnPhases phase) {
		this.phase = phase;
	}
	
	
	//*********** Utils ***********//
	
	public boolean isPushedStart() {
		return pushedStart;
	}
	
	public void setPushedStart(boolean pushedStart) {
		this.pushedStart = pushedStart;
	}
	
	public int getCardIdTakenFromStack() {
		return cardId;
	}
	
	public void setCardIdTakenFromStack(int cardId) {
		this.cardId = cardId;
	}
	
}

