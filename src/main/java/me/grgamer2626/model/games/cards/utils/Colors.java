package me.grgamer2626.model.games.cards.utils;

public enum Colors {
	SPADES(      '♠', "spades"),
	DIAMONDS(    '♦', "diamonds"),
	CLUBS(       '♣', "clubs"),
	HEARTS(      '♥', "hearts");
	
	private final char symbol;
	private final String name;
	
	Colors(char symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public String getName() {
		return name;
	}
}
