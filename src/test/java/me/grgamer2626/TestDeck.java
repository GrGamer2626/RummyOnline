package me.grgamer2626;


import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.decks.Deck;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;

public class TestDeck {
	
	@Test
	public void testDeck_correctSize() {
		//given
		Deck deck = new Deck();
		
		//when
		int correctSize = 108;
		int size = deck.size();
		
		//then
		Assert.isTrue(size == correctSize, "Incorrect deck size!");
	}
	
	@Test
	public void testDeck_allCardUnique() {
		//given
		Deck deck = new Deck();
		Set<Card> set = new HashSet<>(deck);
		
		//when
		int deckSize = deck.size();
		int setSize = set.size();
		
		//then
		Assert.isTrue(deckSize == setSize, "Cards are not unique!");
	}
	@Test
	public void testDeck_Unique() {
		//given
		Deck deck1 = new Deck();
		Deck deck2 = new Deck();
		
		
		//then
		Assert.isTrue(!deck1.equals(deck2), "Decks are identical!");
	}
	
	@Test
	public void testDeck_Shuffle() {
		//given
		Deck deck = new Deck();
		
		Deck cloneDeck = new Deck();
		cloneDeck.clear();
		cloneDeck.addAll(deck);
		
		//then
		Assert.isTrue(deck.equals(cloneDeck), "Deck is not cloned!");
		
		//when
		deck.shuffle();
		
		//then
		Assert.isTrue(!deck.equals(cloneDeck), "Deck is not shuffled!");
	}
	
	@Test
	public void testDeck_pull() {
		//given
		Deck deck = new Deck();
		
		//when
		int initialSize = deck.size();
		Card peekCard = deck.peek();
		Card card = deck.poll();
		
		//then
		Assert.isTrue((initialSize - 1 == deck.size()), "Incorrect deck size!");
		Assert.notNull(card, "Taken card is null!");
		Assert.isTrue(peekCard.equals(card), "The card has not been taken from the top of the deck!");
		Assert.isTrue(!deck.contains(card), "The card has not been removed from the deck!");
	}

	@Test
	public void testDeck_ConstructorWithCollection() {
		//given
		Deque<Card> stack = new LinkedList<>();
		stack.add(new StandardCard(1, Colors.HEARTS, Figures.ACE));
		stack.add(new StandardCard(2, Colors.HEARTS, Figures.KING));
		stack.add(new StandardCard(3, Colors.HEARTS, Figures.QUEEN));
		stack.add(new StandardCard(4, Colors.HEARTS, Figures.JACK));
		stack.add(new StandardCard(5, Colors.HEARTS, Figures._10));
		stack.add(new StandardCard(6, Colors.HEARTS, Figures._9));
		stack.add(new StandardCard(7, Colors.HEARTS, Figures._8));
		
		//when
		Deck deck = new Deck(stack);
		
		//then
		Assert.isTrue(stack.size() == deck.size(), "Deck size is not equal stack size");
		Assert.isTrue(deck.containsAll(stack), "Deck does not contain all cards from stack!");
		Assert.isTrue(!stack.equals(deck), "Deck is not shuffled!");
	}
	
}
