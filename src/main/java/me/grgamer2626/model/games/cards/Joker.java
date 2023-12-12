package me.grgamer2626.model.games.cards;

import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;

public class Joker implements Card {
	
	private final int id;
	private Colors color;
	private Figures figure;
	private final String imgPath;
	private int value = -1;
	private boolean movable;
	
	
	
	public Joker(int id) {
		this(id, false);
	}
	
	public Joker(int id, boolean colored) {
		this.id = id;
		
		imgPath = colored ? "/img/deck/jokers/joker1.png" : "/img/deck/jokers/joker2.png";
		movable = true;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public Colors getColor() {
		return color;
	}
	
	@Override
	public Figures getFigure() {
		return figure;
	}
	
	@Override
	public String getImgPath() {
		return imgPath;
	}
	
	@Override
	public boolean isJoker() {
		return true;
	}
	
	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public boolean isMovable() {
		return movable;
	}
	
	@Override
	public void setMovable(boolean movable) {
		this.movable = movable;
	}
	
	
	/**
	 * Sets color of the card.
	 *
	 * @param color color to be set
	 */
	public void setColor(Colors color) {
		this.color = color;
	}
	
	
	/**
	 * Sets figure of the card and set card value based on that figure.
	 *
	 * @param figure figure to be set
	 */
	public void setFigure(Figures figure) {
		this.figure = figure;
		value = setValue();
	}
	
	private int setValue() {
		if(figure == null) return -1;
		
		int[] possibleValue = figure.getPossibleValue();
		
		if(possibleValue.length > 1) return -1;
		
		return possibleValue[0];
	}
}
