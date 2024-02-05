package me.grgamer2626.sequence;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSorting {
	
	@Test
	public void sorting_CARD_SET() {
		//give
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		Card card2 = new StandardCard(2, Colors.CLUBS, Figures.ACE);
		Card card3 = new StandardCard(3, Colors.SPADES, Figures.ACE);
		Card card4 = new StandardCard(4, Colors.DIAMONDS, Figures.ACE);
		
		sequence.add(card1);
		sequence.add(card2);
		sequence.add(card3);
		sequence.add(card4);
		
		//when
		boolean isCorrectOrder =
				sequence.get(0).equals(card3) &&
				sequence.get(1).equals(card4) &&
				sequence.get(2).equals(card2) &&
				sequence.get(3).equals(card1);
		
		//then
		assertTrue(isCorrectOrder, "Incorrect cart sorting!");
	}
	
	@Test
	public void sorting_SEQUENCE() {
		//give
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card card1  = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		Card card2  = new StandardCard(2, Colors.HEARTS, Figures.QUEEN);
		Card card3  = new StandardCard(3, Colors.HEARTS, Figures._2);
		Card card4  = new StandardCard(4, Colors.HEARTS, Figures.KING);
		Card card5  = new StandardCard(5, Colors.HEARTS, Figures._10);
		Card card6  = new StandardCard(6, Colors.HEARTS, Figures._3);
		Card card7  = new StandardCard(7, Colors.HEARTS, Figures._5);
		Card card8  = new StandardCard(8, Colors.HEARTS, Figures._4);
		Card card9  = new StandardCard(9, Colors.HEARTS, Figures.JACK);
		Card card10 = new StandardCard(10, Colors.HEARTS, Figures._6);
		Card card11 = new StandardCard(11, Colors.HEARTS, Figures._7);
		Card card12 = new StandardCard(12, Colors.HEARTS, Figures._9);
		Card card13 = new StandardCard(13, Colors.HEARTS, Figures._8);
		
		sequence.add(card1);
		sequence.add(card2);
		sequence.add(card3);
		sequence.add(card4);
		sequence.add(card5);
		sequence.add(card6);
		sequence.add(card7);
		sequence.add(card8);
		sequence.add(card9);
		sequence.add(card10);
		sequence.add(card11);
		sequence.add(card12);
		sequence.add(card13);
		
		//when
		boolean isCorrectOrder =
						sequence.get(0).equals(card3) &&
						sequence.get(1).equals(card6) &&
						sequence.get(2).equals(card8) &&
						sequence.get(3).equals(card7) &&
						sequence.get(4).equals(card10) &&
						sequence.get(5).equals(card11) &&
						sequence.get(6).equals(card13) &&
						sequence.get(7).equals(card12) &&
						sequence.get(8).equals(card5) &&
						sequence.get(9).equals(card9) &&
						sequence.get(10).equals(card2) &&
						sequence.get(11).equals(card4) &&
						sequence.get(12).equals(card1);
		
		
		//then
		assertTrue(isCorrectOrder, "Incorrect cart sorting!");
	}
	
	@Test
	public void sorting_AceAsOne() {
		//give
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures._3);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures._2);
		Card card3 = new StandardCard(3, Colors.HEARTS, Figures._5);
		Card card4 = new StandardCard(4, Colors.HEARTS, Figures._4);
		Card card5 = new StandardCard(5, Colors.HEARTS, Figures.ACE);
		
		sequence.add(card1);
		sequence.add(card2);
		sequence.add(card3);
		sequence.add(card4);
		sequence.add(card5);
		
		//when
		boolean isCorrectOrder =
						sequence.get(0).equals(card5) &&
						sequence.get(1).equals(card2) &&
						sequence.get(2).equals(card1) &&
						sequence.get(3).equals(card4) &&
						sequence.get(4).equals(card3);
		
		//then
		assertTrue(isCorrectOrder, "Incorrect cart sorting!");
	}
	
	@Test
	public void sorting_AceAfterKing() {
		//give
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card card1 = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures.QUEEN);
		Card card3 = new StandardCard(3, Colors.HEARTS, Figures._10);
		Card card4 = new StandardCard(4, Colors.HEARTS, Figures.KING);
		Card card5 = new StandardCard(5, Colors.HEARTS, Figures.JACK);
		
		sequence.add(card1);
		sequence.add(card2);
		sequence.add(card3);
		sequence.add(card4);
		sequence.add(card5);
		
		//when
		boolean isCorrectOrder =
						sequence.get(0).equals(card3) &&
						sequence.get(1).equals(card5) &&
						sequence.get(2).equals(card2) &&
						sequence.get(3).equals(card4) &&
						sequence.get(4).equals(card1);
		
		//then
		assertTrue(isCorrectOrder, "Incorrect cart sorting!");
	}
}
