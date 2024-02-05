package me.grgamer2626.sequence;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import static me.grgamer2626.sequence.TestSequence.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestValeCounting {
	
	@Test
	public void valueCounting_Ace_CARD_SET() {
		//give
		Sequence sequence = createCardSet(Figures.ACE);
		
		//when
		int expectedValue = 30;
		int sequenceValue = sequence.getSequenceValue();
		
		//then
		assertEquals(sequenceValue, expectedValue, "Incorrect sequence value!");
	}
	
	@Test
	public void valueCounting_5_CARD_SET() {
		//give
		Sequence sequence = createCardSet(Figures._5);
		
		//when
		int expectedValue = 15;
		int sequenceValue = sequence.getSequenceValue();
		
		//then
		assertEquals(sequenceValue, expectedValue, "Incorrect sequence value!");
	}
	
	@Test
	public void valueCounting_Queen_King_Ace() {
		//give
		Sequence sequence = createQueenKingAceSequence();
		
		//when
		int expectedValue = 30;
		int sequenceValue = sequence.getSequenceValue();
		
		//then
		assertEquals(sequenceValue, expectedValue, "Incorrect sequence value!");
	}
	
	@Test
	public void valueCounting_Ace_2_3() {
		//give
		Sequence sequence = createAce_2_3Sequence();
		
		//when
		int expectedValue = 6;
		int sequenceValue = sequence.getSequenceValue();
		
		//then
		assertEquals(sequenceValue, expectedValue, "Incorrect sequence value!");
	}
	
	@Test
	public void valueCounting_SEQUENCE_8_9_10() {
		//give
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card cardQueen = new StandardCard(1, Colors.HEARTS, Figures._8);
		Card cardKing = new StandardCard(2, Colors.HEARTS, Figures._9);
		Card cardAce = new StandardCard(3, Colors.HEARTS, Figures._10);
		
		sequence.add(cardQueen);
		sequence.add(cardKing);
		sequence.add(cardAce);
		
		//when
		int expectedValue = 27;
		int sequenceValue = sequence.getSequenceValue();
		
		//then
		assertEquals(sequenceValue, expectedValue, "Incorrect sequence value!");
	}
}
