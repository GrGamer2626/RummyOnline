package me.grgamer2626.utils.dto.game.throwCardDto;

import me.grgamer2626.utils.dto.game.CardDto;

public class ThrowCardOutputDto extends ThrowCardDto {
	
	private final CardDto cardDto;
	private final boolean layDown;
	
	public ThrowCardOutputDto(CardDto cardDto, int playerSlot, boolean layDown, int sourceSlot, int sourceNumber) {
		super(playerSlot, sourceSlot, sourceNumber);
		
		this.cardDto = cardDto;
		this.layDown = layDown;
	}
	
	public CardDto getCardDto() {
		return cardDto;
	}
	
	public boolean isLayDown() {
		return layDown;
	}
}
