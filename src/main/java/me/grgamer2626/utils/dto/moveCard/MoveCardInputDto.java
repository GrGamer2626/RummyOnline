package me.grgamer2626.utils.dto.moveCard;

import me.grgamer2626.model.games.cards.utils.Figures;

public class MoveCardInputDto extends MoveCardDto {
	private final int cardId;
	private final Figures figure;
	
	public MoveCardInputDto(int cardId, Figures figure, int playerSlot, int sourceSlot, int sourceNumber, int destinationSlot, int destinationNumber) {
		super(playerSlot, sourceSlot, sourceNumber, destinationSlot, destinationNumber);
		
		this.cardId = cardId;
		this.figure = figure;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getCardId() {
		return cardId;
	}
	
	public Figures getFigure() {
		return figure;
	}
}
