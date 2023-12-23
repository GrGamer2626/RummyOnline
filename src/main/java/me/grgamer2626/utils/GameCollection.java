package me.grgamer2626.utils;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Hand;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;

import java.util.Collection;
import java.util.List;

public interface GameCollection extends List<Card> {
	
	/**
	 * Sort the cards of this game collection. Sorting criteria are established within the class implementing this interface.
	 */
	void sort();

	/**
	 * Returns the Player who owns this collection.
	 *
	 * @return owner of this collection
	 */
	Player getOwner();
	
	
	/**
	 * Every GameCollection has its individual number works like id.
	 * <p>
	 * If the GameCollection is stored in the map, the collection number corresponds to
	 * the key in the map. Otherwise, it value is -1.
	 *
	 * @return number of the GameCollection
	 */
	int getNumber();
	
	/**
	 * Returns the slot number of the player to which this collection belongs.
	 *
	 * @return slot number
	 */
	int getSlot();
	
	/**
	 * Returns the card at the specified position in this collection and remove it from them.
	 *
	 * @param index index of the card to return
	 * @return the card at the specified position in this list
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
	default Card pull(int index) throws IndexOutOfBoundsException {
		Card card = get(index);
		remove(card);
		
		return card;
	}
	
	/**
	 * Returns the card with the specified id from this collection and removes it.
	 *
	 * @param cardId the id of the card to return
	 * @return the card with the specified id
	 * @throws IllegalArgumentException if the list does not contain a card with the specified id
	 */
	default Card pullById(int cardId) throws IllegalArgumentException {
		int index = -1;
		for(Card card : this) {
			if(card.getId() == cardId) {
				index = indexOf(card);
				break;
			}
		}
		if(index == -1) throw new IllegalArgumentException("The list does not contain card with id " + cardId);
		
		return pull(index);
	}
	
	/**
	 * Returns the card with the specified id from this collection.
	 *
	 * @param cardId the id of the card to return
	 * @return the card with the specified id
	 * @throws IllegalArgumentException if the list does not contain a card with the specified id
	 */
	default Card getById(int cardId) throws IllegalArgumentException {
		return stream()
				.filter(card-> card.getId() == cardId)
				.findFirst()
				.orElseThrow(()-> new IllegalArgumentException("The list does not contain card with id " + cardId));
	}

	/**
	 * Returns true if this collection contain a Card with specified id.
	 *
	 * @param cardId id of the card
	 * @return true if contains card with this id, otherwise false
	 */
	default boolean containId(int cardId) {
		return stream()
				.map(Identifiable::getId)
				.anyMatch(id-> id == cardId);
	}
	
	/**
	 * Returns the card with the specified figure and color from this collection and removes it.
	 *
	 * @param figure the figure of card to return
	 * @param color the color of the card to return
	 * @return the card with the specified figure and color
	 * @throws IllegalArgumentException if the collection does not contain a card with the specified figure and color
	 */
	default Card pullByDetails(Figures figure, Colors color) {
		Card card = getByDetails(figure, color);
		remove(card);
		
		return card;
	}
	
	/**
	 * Returns the card with the specified figure and color from this collection.
	 *
	 * @param figure the figure of card to return
	 * @param color the color of the card to return
	 * @return the card with the specified figure and color
	 * @throws IllegalArgumentException if the collection does not contain a card with the specified figure and color
	 */
	default Card getByDetails(Figures figure, Colors color) {
		return stream()
				.filter(card -> card.getFigure() == figure && card.getColor() == color)
				.findFirst()
				.orElseThrow(()-> new IllegalArgumentException("The list does not contain Joker with figure "+ figure+" and color "+color));
	}
	
	/**
	 * Returns true if this collection contain a card with specified figure and color.
	 *
	 * @param figure the figure of card to return
	 * @param color the color of the card to return
	 * @return true if contains card with this figure and color, otherwise false
	 */
	default boolean containsDetails(Figures figure, Colors color) {
		return stream().anyMatch(card -> card.getFigure() == figure && card.getColor() == color);
	}
	
	/**
	 * Move the card with the specified id from this list to specified destination list.
	 *
	 * @param cardId the id of the card to move
	 * @param destination the collection to which the card is to be moved
	 * @return moved card
	 * @throws IllegalArgumentException if the list does not contain a card with the specified id
	 */
	default Card moveTo(int cardId, Collection<Card> destination) {
		Card card = pullById(cardId);
		destination.add(card);
		
		return card;
	}
	
	/**
	 * Move the Card with the specified id from this list to specified destination list, and set its figure if is
	 * instance of Joker.
	 *
	 * @param cardId the id of the card to move
	 * @param figure figure to set
	 * @param destination the collection to which the card is to be moved
	 * @return moved card
	 * @throws IllegalArgumentException if the list does not contain a card with the specified id or card with specified id is not joker
	 */
	default Joker moveTo(int cardId, Figures figure, Collection<Card> destination) throws IllegalArgumentException {
		Card card = pullById(cardId);
		
		if(!card.isJoker()) throw new IllegalArgumentException("Card with specified id is not instance of Joker");
		
		Joker joker = (Joker) card;
		joker.setFigure(figure);
		
		destination.add(card);
		
		return joker;
	}
	
	
	
	default boolean containsJoker() {
		return stream().anyMatch(Card::isJoker);
	}
	
	default boolean isSequence() {
		return this instanceof Sequence;
	}
	
	default boolean isHand() {
		return this instanceof Hand;
	}
}
