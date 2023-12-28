package me.grgamer2626.service.games;

import me.grgamer2626.model.games.Game;
import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.utils.dto.CardDto;
import me.grgamer2626.utils.dto.LayDownDto;
import me.grgamer2626.utils.dto.ReturnCardDto;
import me.grgamer2626.utils.dto.moveCard.MoveCardDto;
import me.grgamer2626.utils.dto.moveCard.MoveCardInputDto;
import me.grgamer2626.utils.dto.moveCard.ReplaceJokerDto;
import me.grgamer2626.utils.dto.takeCard.TakeCardDto;
import me.grgamer2626.utils.dto.throwCardDto.ThrowCardDto;
import me.grgamer2626.utils.dto.throwCardDto.ThrowCardInputDto;

import java.util.Map;

public interface GameService {
	
	Game getGame(long tableId);
	
	Map<Integer, Integer> startGame(long tableId);
	
	Card takeFromDeck(long tableId, String playerName);
	
	Card takeFromStack(long tableId, String playerName);
	
	TakeCardDto createTakeCardDto(long tableId, int slot, Card card);
	
	CardDto confirmTakenCard(long tableId, String playerName);
	
	ReturnCardDto returnCardFromStack(long tableId, String playerName);
	
	MoveCardDto moveCard(long tableId, String playerName, MoveCardInputDto moveCardDto);
	
	MoveCardDto moveCard(long tableId, MoveCardInputDto moveCardDto);
	
	ReplaceJokerDto replaceJoker(long tableId, String playerName, MoveCardInputDto moveCardDto);
	
	ThrowCardDto throwCard(long tableId, String playerName, ThrowCardInputDto throwCardDto);
	
	
	LayDownDto layDown(long tableId, String playerName);
	
	Map<Integer, Boolean> layDown(long tableId);
}
