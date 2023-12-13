package me.grgamer2626.utils.dto.throwCardDto;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.utils.GameCollection;

public class ThrowCardDto {
	
	
	private final int playerSlot;
	private final int sourceSlot;
	private final int sourceNumber;
	
	public ThrowCardDto(int playerSlot, int sourceSlot, int sourceNumber) {
		this.playerSlot = playerSlot;
		this.sourceSlot = sourceSlot;
		this.sourceNumber = sourceNumber;
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
	
	
	public GameCollection getSource(Game game) {
		return getGameCollection(game, sourceSlot, sourceNumber);
	}
	
	private GameCollection getGameCollection(Game game, int slot, int number) {
		Player player = game.getPlayerSlots().get(slot);
		return isHand(number) ? player.getOnHand() : player.getSequence(number);
	}
	
	private boolean isHand(int sourceNumber) {
		return sourceNumber == -1;
	}
	
}
