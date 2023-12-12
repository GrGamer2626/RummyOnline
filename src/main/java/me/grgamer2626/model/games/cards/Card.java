package me.grgamer2626.model.games.cards;

import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.utils.Identifiable;

public interface Card extends Identifiable<Integer> {
	
	
	/**
	 * Return color of the card. If card is instance of joker, it may return null and
	 * its color is determined to be set based on its position in the sequence.
	 *
	 * @return card color or null
	 */
	Colors getColor();
	
	/**
	 * Return figure of the card. If card is instance of joker, it may return null and
	 * its figure is determined to be set based on its position in the sequence.
	 *
	 * @return card color or null
	 */
	Figures getFigure();
	
	/**
	 * Return path to the card image.
	 *
	 * @return path to image.
	 */
	String getImgPath();
	
	/**
	 * Returns true if this card is instance of Joker.
	 *
	 * @return true if is instance of Joker, otherwise false
	 */
	boolean isJoker();
	
	/**
	 * Returns the value of the card based on its figure. If the card figure has more
	 * than one possible value or the card figure is null, return -1 and its value is
	 * determined to be set based on its position in the sequence.
	 *
	 * @return value of the card
	 */
	int getValue();
	
	/**
	 * Set value of the card.
	 *
	 * @param value value to be set
	 */
	void setValue(int value);
	
	/**
	 * Returns true if the card can be moved. Cards in hand or added to the sequences in
	 * current turn can be moved.
	 *
	 * @return true if card can be moved, otherwise false.
	 */
	boolean isMovable();
	
	/**
	 * Set if the card can be moved.
	 *
	 * @param movable value to be set
	 */
	void setMovable(boolean movable);
	
}
