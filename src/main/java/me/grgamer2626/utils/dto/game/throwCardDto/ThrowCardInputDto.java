package me.grgamer2626.utils.dto.game.throwCardDto;

public class ThrowCardInputDto extends ThrowCardDto {
	
	private final int cardId;
	public ThrowCardInputDto(int cardId, int playerSlot, int sourceSlot, int sourceNumber) {
		super(playerSlot, sourceSlot, sourceNumber);
		
		this.cardId = cardId;
	}
	
	public int getCardId() {
		return cardId;
	}
}
