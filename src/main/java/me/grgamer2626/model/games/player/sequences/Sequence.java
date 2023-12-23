package me.grgamer2626.model.games.player.sequences;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.utils.GameCollection;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Sequence extends ArrayList<Card> implements GameCollection {
	
	private final Player owner;
	private final int number;
	private final int slot;
	private SequenceType type;
	private int sequenceValue;
	
	public Sequence(int number, Player owner) {
		this.owner = owner;
		this.number = number;
		this.slot = owner.getSlot();
		sequenceValue = 0;
		sort();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void sort() {
		if(size() < 2) return;
		
		sort(Comparator.comparing(card -> {
			if(card != null) {
				Figures figure = card.getFigure();
				if(figure != null) {
					return figure.ordinal();
				}
			}
			 return -1;
		}));
		int lastIndex = size() - 1;
		Card card = get(lastIndex);
		
		if(card.getFigure() == Figures.ACE) {
			Card beforeACE = get(lastIndex - 1);
			if(beforeACE.getFigure() != Figures.KING) {
				sort(Comparator.comparing(c -> c.getFigure() == Figures.ACE ? 0 : 1));
				card.setValue(Figures.ACE.getPossibleValue()[0]);
				return;
			}
			card.setValue(card.getFigure().getPossibleValue()[1]);
		}
	}
	
	@Override
	public Player getOwner() {
		return owner;
	}
	
	@Override
	public int getNumber() {
		return number;
	}
	
	@Override
	public int getSlot() {
		return slot;
	}
	
	public int getSequenceValue() {
		return sequenceValue;
	}
	
	@Override
	public boolean add(Card card) throws IllegalArgumentException {
		if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		
		boolean added = super.add(card);
		if(added) {
			sort();
			
			if(size() >= 3) {
				setType();
			}
			sequenceValue += card.getValue();
		}
		return added;
	}
	
	@Override
	public void add(int index, Card card) throws IllegalArgumentException {
		if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		
		super.add(index, card);
		sort();
		
		type = null;
		
		if(size() >= 3) {
			setType();
		}
		sequenceValue += card.getValue();
	}
	
	@Override
	public boolean addAll(Collection<? extends Card> cards) throws IllegalArgumentException {
		for(Card card: cards) {
			if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		}
		
		boolean added = super.addAll(cards);
		if(added) {
			sort();
			
			type = null;
			
			if(size() >= 3) {
				setType();
			}
			for(Card card : cards) {
				sequenceValue += card.getValue();
			}
		}
		return added;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Card> cards) throws IllegalArgumentException {
		for(Card card: cards) {
			if(containId(card.getId())) throw new IllegalArgumentException("The hand contains card with id: "+card.getId());
		}
		
		boolean added = super.addAll(index, cards);
		if(added) {
			sort();
			
			type = null;
			
			if(size() >= 3) {
				setType();
			}
			for(Card card : cards) {
				sequenceValue += card.getValue();
			}
		}
		
		return added;
	}
	
	@Override
	public Card remove(int index) {
		Card card = super.remove(index);
		
		type = null;
		
		if(size() >= 3) {
			setType();
		}
		sequenceValue -= card.getValue();
		
		return card;
	}
	
	@Override
	public boolean remove(Object card) throws IllegalArgumentException {
		if(!(card instanceof Card)) throw new IllegalArgumentException("Incorrect data type.");
		
		boolean removed = super.remove(card);
		if(removed) {
			
			type = null;
			
			if(size() >= 3) {
				setType();
			}
			sequenceValue -= ((Card)card).getValue();
		}
		
		return removed;
	}
	
	public SequenceType getType() {
		return type;
	}
	
	public boolean isSequenceCorrect() {
		return type != null || isEmpty();
	}
	
	public boolean isClear() {
		return type == SequenceType.CLEAR_SEQUENCE;
	}
	
	private void setType() {
		type = null;
		
		if(size() < 3) return;
		
		Function<Card, Colors> colors = Card::getColor;
		
		if(isCardSet(Card::getFigure, colors)) {
			type = SequenceType.CARD_SET;
			sort(Comparator.comparing(Card::getColor));
			
			for(Card card: this) {
				Figures figure = card.getFigure();
				if(figure != Figures.ACE) break;
				
				card.setValue(figure.getPossibleValue()[1]);
			}
			return;
		}
		
		if(!containGaps()) {
			if(!containsJoker() && isIdentical(colors)) {
				type = SequenceType.CLEAR_SEQUENCE;
				return;
			}
			
			Colors color = getColor();
			for(Card card : this) {
				if(!card.isJoker()) continue;
				((Joker) card).setColor(color);
			}
			if(isIdentical(colors)) type = SequenceType.UNCLEAR_SEQUENCE;
		}
	}
	
	
	private Colors getColor() {
		Colors color = null;
		for(Card card : this) {
			if(card.isJoker()) continue;
			color = card.getColor();
			break;
		}
		return color;
	}

	private boolean isCardSet(Function<Card, Figures> figures, Function<Card, Colors> colors) {
		if(isIdentical(figures)) {
			if(containsJoker()) {
				List<Colors> missingColors = getMissingColors();
				for(Card card: this) {
					if(!card.isJoker()) continue;
					
					if(missingColors.isEmpty()) return false;
					
					((Joker) card).setColor(missingColors.get(0));
					missingColors.remove(0);
				}
			}
			return areAllDifferent(colors);
		}
		return false;
	}
	
	private <T> boolean isIdentical(Function<Card, T> function) {
		Set<T> set = getSet(function);
		
		return set.size() == 1;
	}
	
	private <T>  boolean areAllDifferent(Function<Card, T> function) {
		Set<T> set = getSet(function);
		
		return set.size() == this.size();
		
	}
	
	private <T> Set<T> getSet(Function<Card, T> function) {
		return stream()
				.map(function)
				.collect(Collectors.toSet());
	}
	
	private boolean containGaps() {
		for(int i = 0 ; i < size() - 1 ; i++) {
			Figures figure = get(i).getFigure();
			Figures nextFigure = get(i + 1).getFigure();
			
			if(figure==Figures.ACE && nextFigure==Figures._2) continue;
			
			if(nextFigure.ordinal() - figure.ordinal() != 1) return true;
		}
		return false;
	}

	private List<Colors> getMissingColors() {
		List<Colors> allColors = Arrays.stream(Colors.values()).collect(Collectors.toList());
		stream()
				.map(Card::getColor)
				.forEach(allColors::remove);
		
		return allColors;
	}
}
