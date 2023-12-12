package me.grgamer2626.model.games.player;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.utils.GameCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public final class Hand extends ArrayList<Card> implements GameCollection {
	
	private final Player owner;
	
	public Hand(Player owner) {
		this.owner = owner;
		sort();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void sort() {
		if(size() < 2) return;
		
		this.sort(Comparator
				.comparing((Card card)-> {
					if(card != null) {
						Colors color = card.getColor();
						if(color != null) {
							return color.ordinal();
						}
					}
					return -1;
					
				}).thenComparing((Card card)-> {
					if(card != null) {
						Figures figure = card.getFigure();
						if(figure != null) {
							return figure.ordinal();
						}
					}
					return -1;
					
				}));
	}
	
	@Override
	public Player getOwner() {
		return owner;
	}
	
	@Override
	public boolean add(Card card) throws IllegalArgumentException {
		if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		
		boolean added = super.add(card);
		if(added) sort();
		
		return added;
	}
	
	@Override
	public void add(int index, Card card) throws IllegalArgumentException {
		if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		
		super.add(index, card);
		sort();
	}
	
	@Override
	public boolean addAll(Collection<? extends Card> cards) throws IllegalArgumentException {
		for(Card card: cards) {
			if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		}
		boolean added = super.addAll(cards);
		if (added) sort();
		
		return added;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Card> cards) throws IllegalArgumentException {
		for(Card card: cards) {
			if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		}
		boolean added = super.addAll(index, cards);
		if (added) sort();
		
		return added;
	}
	
}
