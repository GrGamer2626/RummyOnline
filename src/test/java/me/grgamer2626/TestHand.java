package me.grgamer2626;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Hand;
import me.grgamer2626.model.games.player.Player;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestHand {
	
	private final Player player = new Player(1, "Tester", 1);
	
	@Test
	public void testHand_addCard() {
		//given
		Hand hand = new Hand(player);
		Card card = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		
		//then
		assertTrue(hand.add(card), "The card has not been added!");
		assertTrue(hand.contains(card), "The hand  does not contain an added cards!");
	}
	
	@Test
	public void testHand_addExistingCard() {
		//given
		Hand hand = new Hand(player);
		Card card = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		hand.add(card);
		
		//then
		assertThrows(IllegalArgumentException.class, ()-> hand.add(card), "An existing card has been added!");
	}
	
	@Test
	public void testHand_sorting() {
		//give
		Hand hand = new Hand(player);
		
		Card joker = new Joker(1);
		
		Card spadesKing = new StandardCard(2, Colors.SPADES, Figures.KING);
		Card spadesAce = new StandardCard(3, Colors.SPADES, Figures.ACE);
		
		Card diamondsKing = new StandardCard(4, Colors.DIAMONDS, Figures.KING);
		Card diamondsAce = new StandardCard(5, Colors.DIAMONDS, Figures.ACE);
		
		Card clubs9 = new StandardCard(6, Colors.CLUBS, Figures._9);
		Card clubs10 = new StandardCard(7, Colors.CLUBS, Figures._10);
		Card clubsAce = new StandardCard(8, Colors.CLUBS, Figures.ACE);
		
		Card harts2 = new StandardCard(9, Colors.HEARTS, Figures._2);
		Card hartsAce = new StandardCard(10, Colors.HEARTS, Figures.ACE);
		
		//when
		hand.add(spadesAce);
		hand.add(clubs9);
		hand.add(clubs10);
		hand.add(diamondsKing);
		hand.add(harts2);
		hand.add(joker);
		hand.add(clubsAce);
		hand.add(diamondsAce);
		hand.add(spadesKing);
		hand.add(hartsAce);
		
		boolean correctSorted =
						hand.get(0).equals(joker) &&
						hand.get(1).equals(spadesKing) &&
						hand.get(2).equals(spadesAce) &&
						hand.get(3).equals(diamondsKing) &&
						hand.get(4).equals(diamondsAce) &&
						hand.get(5).equals(clubs9) &&
						hand.get(6).equals(clubs10) &&
						hand.get(7).equals(clubsAce) &&
						hand.get(8).equals(harts2) &&
						hand.get(9).equals(hartsAce);
		
		//then
		assertTrue(correctSorted, "Incorrect sorting!");
	}
}
