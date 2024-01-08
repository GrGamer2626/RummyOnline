package me.grgamer2626.utils.dto.game.takeCard;

import me.grgamer2626.utils.dto.game.CardDto;

import java.util.Map;

public class TakeCardDto {
	
	private final CardDto card;
	private final int position;
	private final Map<Integer, Boolean> layDownPlayers;
	
	public TakeCardDto(CardDto card, int position, Map<Integer, Boolean> layDownPlayers) {
		this.card = card;
		this.position = position;
		this.layDownPlayers = layDownPlayers;
	}
	
	public CardDto getCardDto() {
		return card;
	}
	
	public int getPosition() {
		return position;
	}
	
	public Map<Integer, Boolean> getLayDownPlayers() {
		return layDownPlayers;
	}
}
