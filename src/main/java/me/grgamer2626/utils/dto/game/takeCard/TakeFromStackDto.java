package me.grgamer2626.utils.dto.game.takeCard;

import me.grgamer2626.utils.dto.game.CardDto;

public class TakeFromStackDto {
	
	private int playerSlot;
	private CardDto cardDto;
	
	public TakeFromStackDto(int playerSlot, CardDto cardDto) {
		this.playerSlot = playerSlot;
		this.cardDto = cardDto;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public int getPlayerSlot() {
		return playerSlot;
	}
	
	public CardDto getCardDto() {
		return cardDto;
	}
}
