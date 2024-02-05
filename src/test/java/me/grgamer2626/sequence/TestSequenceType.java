package me.grgamer2626.sequence;

import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import me.grgamer2626.model.games.player.sequences.SequenceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSequenceType extends TestSequence {
	
	private final Player player = new Player(1, "Tester", 1);
	
	@Test
	public void sequenceType_CARD_SET() {
		//give
		Sequence sequence = createCardSet(Figures.ACE);
		
		//then
		assertEquals(SequenceType.CARD_SET, sequence.getType(), "Incorrect sequence type! Expected type: CARD_SET");
	}
	
	@Test
	public void sequenceType_CLEAR_SEQUENCE() {
		//give
		Sequence sequence = createQueenKingAceSequence();
		
		//then
		assertEquals(SequenceType.CLEAR_SEQUENCE, sequence.getType(), "Incorrect sequence type! Expected type: CLEAR_SEQUENCE");
	}
	
	@Test
	public void sequenceType_UNCLEAR_SEQUENCE() {
		//give
		Sequence sequence = createUnclearSequence();
		
		//then
		assertEquals(SequenceType.UNCLEAR_SEQUENCE, sequence.getType(), "Incorrect sequence type! Expected type: UNCLEAR_SEQUENCE");
	}
	
	@Test
	public void sequenceType_IncorrectSequenceSize() {
		//give
		Sequence sequence = new Sequence(1, player);
		
		sequence.add(new StandardCard(1, Colors.HEARTS, Figures._4));
		sequence.add(new StandardCard(2, Colors.HEARTS, Figures._5));
		
		//then
		assertNull(sequence.getType(), "Incorrect sequence type! Expected type: null");
	}
	
	@Test
	public void sequenceType_CARD_SET_RepeatedColor() {
		//give
		Sequence sequence = new Sequence(1, player);
		
		sequence.add(new StandardCard(1, Colors.HEARTS, Figures.ACE));
		sequence.add(new StandardCard(2, Colors.HEARTS, Figures.ACE));
		sequence.add(new StandardCard(3, Colors.SPADES, Figures.ACE));
		//then
		assertNull(sequence.getType(), "Incorrect sequence type! Expected type: null");
	}
	
	@Test
	public void sequenceType_SEQUENCE_ContainsGaps() {
		//give
		Sequence sequence = new Sequence(1, player);
		
		sequence.add(new StandardCard(1, Colors.HEARTS, Figures._2));
		sequence.add(new StandardCard(2, Colors.HEARTS, Figures.ACE));
		sequence.add(new StandardCard(3, Colors.HEARTS, Figures._3));
		sequence.add(new StandardCard(4, Colors.HEARTS, Figures._7));
		
		//then
		assertNull(sequence.getType(), "Incorrect sequence type! Expected type: null");
	}
	
	@Test
	public void sequenceType_SEQUENCE_NotTheSameColor() {
		//give
		Sequence sequence = new Sequence(1, player);
		
		sequence.add(new StandardCard(1, Colors.HEARTS, Figures._4));
		sequence.add(new StandardCard(2, Colors.SPADES, Figures._5));
		sequence.add(new StandardCard(3, Colors.HEARTS, Figures._6));
		
		//then
		assertNull(sequence.getType(), "Incorrect sequence type! Expected type: null");
	}
	
	@Test
	public void sequenceType_Unordered() {
		//give
		Sequence sequence = new Sequence(1, player);
		
		sequence.add(new StandardCard(1, Colors.HEARTS, Figures._2));
		sequence.add(new StandardCard(2, Colors.SPADES, Figures.ACE));
		sequence.add(new StandardCard(3, Colors.HEARTS, Figures.JACK));
		sequence.add(new StandardCard(4, Colors.DIAMONDS, Figures._7));
		
		//then
		assertNull(sequence.getType(), "Incorrect sequence type! Expected type: null");
	}
}
