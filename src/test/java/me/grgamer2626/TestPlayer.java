package me.grgamer2626;

import static org.junit.jupiter.api.Assertions.*;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestPlayer {
	
	/**
	 * All Sequence Empty
	 */
	@Test
	public void testLayDown_AllSequenceEmpty() {
		//give
		Player player = new Player(1, "Tester", 1);
		
		//then
		assertFalse(player.isLayDown(), "Player should not be lay down at start!");
		assertFalse(player.layDown(), "Laying down result should be false when all sequences are empty!");
		assertFalse(player.isLayDown(), "Player should still not be laid down after laying down when all sequences are empty!");
	}

	/**
	 * CLEAR_SEQUENCE
	 * Total >= 51
	 */
	@Test
	public void testLayDown_AllConditionsMet() {
		//give
		Player player = new Player(1, "Tester", 1);
		
		Sequence sequence1 = player.getSequence(1);
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures._9);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures._10);
		Joker card3 = new Joker(3);
		card3.setFigure(Figures.JACK);
		
		Card[] cards1 = {card1, card2, card3};
		sequence1.addAll(List.of(cards1));
		
		Sequence sequence2 = player.getSequence(2);
		Card card4 = new StandardCard(4, Colors.SPADES, Figures.QUEEN);
		Card card5 = new StandardCard(5, Colors.SPADES, Figures.KING);
		Card card6 = new StandardCard(6, Colors.SPADES, Figures.ACE);
		
		Card[] cards2 = {card4, card5, card6};
		sequence2.addAll(List.of(cards2));
		
		//then
		assertTrue(player.layDown(), "Player should be laid down when all conditions are met!");
		assertTrue(player.isLayDown(), "Player should be laid down after laying down when all conditions are met!");
	}
	
	/**
	 * NO CLEAR_SEQUENCE
	 * Total < 51
	 */
	@Test
	public void testLayDown_NoConditionsMet() {
		//give
		Player player = new Player(1, "Tester", 1);
		
		Sequence sequence1 = player.getSequence(1);
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures._9);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures._10);
		Joker card3 = new Joker(3);
		card3.setFigure(Figures.JACK);
		
		Card[] cards1 = {card1, card2, card3};
		sequence1.addAll(List.of(cards1));
		
		Sequence sequence2 = player.getSequence(2);
		Card card4 = new StandardCard(4, Colors.SPADES, Figures._2);
		Card card5 = new StandardCard(5, Colors.DIAMONDS, Figures._2);
		Card card6 = new StandardCard(6, Colors.CLUBS, Figures._2);
		
		Card[] cards2 = {card4, card5, card6};
		sequence2.addAll(List.of(cards2));
		
		//then
		assertFalse(player.layDown(), "Player should not be laid down when no conditions are met!");
		assertFalse(player.isLayDown(), "Player should not be laid down after laying down when no conditions are met!");
	}
	
	/**
	 * NO CLEAR_SEQUENCE
	 * Total >= 51
	 */
	@Test
	public void testLayDown_ValueOver51_NoClearSequences() {
		//give
		Player player = new Player(1, "Tester", 1);
		
		Sequence sequence1 = player.getSequence(1);
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures._9);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures._10);
		Joker card3 = new Joker(3);
		card3.setFigure(Figures.JACK);
		
		Card[] cards1 = {card1, card2, card3};
		sequence1.addAll(List.of(cards1));
		
		Sequence sequence2 = player.getSequence(2);
		Card card4 = new StandardCard(4, Colors.SPADES, Figures.ACE);
		Card card5 = new StandardCard(5, Colors.HEARTS, Figures.ACE);
		Card card6 = new StandardCard(6, Colors.DIAMONDS, Figures.ACE);
		
		Card[] cards2 = {card4, card5, card6};
		sequence2.addAll(List.of(cards2));
		
		//then
		assertFalse(player.layDown(), "Player should not be laid down when no clear sequences!");
		assertFalse(player.isLayDown(), "Player should not be laid down after laying down when no clear sequences!");
	}
	
	/**
	 * CLEAR_SEQUENCE
	 * Total < 51
	 */
	@Test
	public void testLayDown_ValueBelow51_ClearSequences() {
		//give
		Player player = new Player(1, "Tester", 1);
		Sequence sequence1 = player.getSequence(1);
		
		Card card1 = new StandardCard(1, Colors.SPADES, Figures.QUEEN);
		Card card2 = new StandardCard(2, Colors.SPADES, Figures.KING);
		Card card3 = new StandardCard(3, Colors.SPADES, Figures.ACE);
		
		Card[] cards1 = {card1, card2, card3};
		sequence1.addAll(List.of(cards1));
		
		//then
		assertFalse(player.layDown(), "Player should not be laid down when value is below 51!");
		assertFalse(player.isLayDown(), "Player should not be laid down after laying down when value is below 51!");
	}
	
	/**
	 * CLEAR_SEQUENCE
	 * Total >= 51
	 * One Sequence is Incorrect
	 */
	@Test
	public void testLayDown_IncorrectSequence() {
		//give
		Player player = new Player(1, "Tester", 1);
		
		Sequence sequence1 = player.getSequence(1);
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures._9);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures._10);
		Joker card3 = new Joker(3);
		card3.setFigure(Figures.JACK);
		
		Card[] cards1 = {card1, card2, card3};
		sequence1.addAll(List.of(cards1));
		
		Sequence sequence2 = player.getSequence(2);
		Card card4 = new StandardCard(4, Colors.SPADES, Figures.QUEEN);
		Card card5 = new StandardCard(5, Colors.SPADES, Figures.KING);
		Card card6 = new StandardCard(6, Colors.SPADES, Figures.ACE);
		
		Card[] cards2 = {card4, card5, card6};
		sequence2.addAll(List.of(cards2));
		
		Sequence sequence3 = player.getSequence(3);
		Card card7 = new StandardCard(7, Colors.SPADES, Figures.JACK);
		Card card8 = new StandardCard(8, Colors.DIAMONDS, Figures._5);
		
		Card[] cards3 = {card7, card8};
		sequence3.addAll(List.of(cards3));
		
		//then
		assertFalse(player.layDown(), "Player should not be laid down when one sequence is incorrect!");
		assertFalse(player.isLayDown(), "Player should not be laid down after laying down when one sequence is incorrect!");
	}
}
