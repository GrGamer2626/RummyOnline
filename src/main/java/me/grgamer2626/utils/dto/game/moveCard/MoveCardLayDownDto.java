package me.grgamer2626.utils.dto.game.moveCard;

import me.grgamer2626.utils.dto.game.CardDto;

public class MoveCardLayDownDto extends MoveCardDto {
	
	private final CardDto cardDto;
	private final int position;
	
	public MoveCardLayDownDto(CardDto cardDto, int position, int playerSlot, int sourceSlot, int sourceNumber, int destinationSlot, int destinationNumber) {
		super(playerSlot, sourceSlot, sourceNumber, destinationSlot, destinationNumber);
		
		this.cardDto = cardDto;
		this.position = position;
	}
	
	public MoveCardLayDownDto(CardDto cardDto, int position, MoveCardInputDto inputDto) {
		super(  inputDto.getPlayerSlot(),
				inputDto.getSourceSlot(),
				inputDto.getSourceNumber(),
				inputDto.getDestinationSlot(),
				inputDto.getDestinationNumber());
		
		this.cardDto = cardDto;
		this.position = position;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CardDto getCardDto() {
		return cardDto;
	}
	
	public int getPosition() {
		return position;
	}
}
