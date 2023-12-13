package me.grgamer2626.utils.dto.moveCard;

public class MoveCardOutputDto extends MoveCardDto {
	
	private final int cardId;
	private final int position;
	
	
	public MoveCardOutputDto(int cardId, int position, int playerSlot, int sourceSlot, int sourceNumber, int destinationSlot, int destinationNumber) {
		super(playerSlot, sourceSlot, sourceNumber, destinationSlot, destinationNumber);
		this.cardId = cardId;
		this.position = position;
	}
	
	public MoveCardOutputDto(int position, MoveCardInputDto inputDto) {
		super(  inputDto.getPlayerSlot(),
				inputDto.getSourceSlot(),
				inputDto.getSourceNumber(),
				inputDto.getDestinationSlot(),
				inputDto.getDestinationNumber());
		
		this.cardId = inputDto.getCardId();
		this.position = position;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getCardId() {
		return cardId;
	}
	
	public int getPosition() {
		return position;
	}
}
