package me.grgamer2626.utils.dto.moveCard;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.utils.GameCollection;

public class MoveCardDto {
	
	private final int playerSlot;
	private final int sourceSlot;
	private final int sourceNumber;
	
	private final int destinationSlot;
	private final int destinationNumber;
	
	public MoveCardDto(int playerSlot, int sourceSlot, int sourceNumber, int destinationSlot, int destinationNumber) {
		this.playerSlot = playerSlot;
		this.sourceSlot = sourceSlot;
		this.sourceNumber = sourceNumber;
		this.destinationSlot = destinationSlot;
		this.destinationNumber = destinationNumber;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getPlayerSlot() {
		return playerSlot;
	}
	
	public int getSourceSlot() {
		return sourceSlot;
	}
	
	public int getSourceNumber() {
		return sourceNumber;
	}
	
	public int getDestinationSlot() {
		return destinationSlot;
	}
	
	public int getDestinationNumber() {
		return destinationNumber;
	}
	
	public GameCollection getSource(Game game) {
		return getGameCollection(game, sourceSlot, sourceNumber);
	}
	
	public GameCollection getDestination(Game game) {
		return getGameCollection(game, destinationSlot, destinationNumber);
	}
	
	
	private GameCollection getGameCollection(Game game, int slot, int number) {
		Player player = game.getPlayerSlots().get(slot);
		return isHand(number) ? player.getOnHand() : player.getSequence(number);
	}
	
	private boolean isHand(int sourceNumber) {
		return sourceNumber == -1;
	}
}
