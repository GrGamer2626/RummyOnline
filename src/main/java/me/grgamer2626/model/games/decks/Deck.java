package me.grgamer2626.model.games.decks;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public final class Deck extends LinkedList<Card> {
	private Random random;
	public Deck() {
		super();
		random = new Random();
		
		
		createStandardDeck();
		
		
		Collections.shuffle(this, random);
	}
	
	public Deck(Collection<? extends Card> c) {
		super(c);
		random = new Random();
		
		Collections.shuffle(this, random);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void createStandardDeck() {
		int id = 1;
		for (int i = 0 ; i < 2 ; i++) {
			for (Colors color : Colors.values()) {
				for (Figures figure : Figures.values()) {
					Card card = new StandardCard(id, color, figure);
					add(card);
					id++;
				}
			}
			add(new Joker(++id, true));
			add(new Joker(++id));
		}
	}
	
	@Override
	public Card poll() {
		Card card = super.poll();
		
		if (card != null) remove(card);
		
		return card;
	}
	
	public void shuffle() {
		random = new Random();
		Collections.shuffle(this, random);
	}
}
