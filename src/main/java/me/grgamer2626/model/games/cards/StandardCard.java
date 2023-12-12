package me.grgamer2626.model.games.cards;

import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;

public class StandardCard implements Card {
	
	private final int id;
	private final Colors color;
	private final Figures figure;
	private final String imgPath;
	private int value;
	private boolean movable;
	
	public StandardCard(int id, Colors color, Figures figure) {
		this.id = id;
		this.color = color;
		this.figure = figure;
		
		imgPath = "/img/deck/"+color.getName()+"/"+figure.getName()+".png";
		value = setValue();
		movable = true;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private int setValue() {
		int[] possibleValue = figure.getPossibleValue();
		
		if(possibleValue.length > 1) return -1;
		
		return possibleValue[0];
	}
	
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
		return false;
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
}
