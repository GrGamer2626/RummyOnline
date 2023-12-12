package me.grgamer2626.model.games.cards.utils;

public enum Figures {
	
	_2("2", "2", 2),
	_3("3", "3", 3),
	_4("4",  "4",4),
	_5("5", "5",5),
	_6("6", "6",6),
	_7("7", "7",7),
	_8("8", "8",8),
	_9("9", "9",9),
	_10("10", "10",10),
	JACK("J", "jack",10),
	QUEEN("Q", "queen",10),
	KING("K", "king",10),
	ACE("A", "ace", 1, 10);
	
	private final String symbol;
	private final String name;
	private final int[] possibleValue;
	
	Figures(String symbol, String name, int... possibleValue) {
		this.symbol = symbol;
		this.name = name;
		this.possibleValue = possibleValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getSymbol() {
		return symbol;
	}
	
	public String getName() {
		return name;
	}
	
	public int[] getPossibleValue() {
		return possibleValue;
	}
	
}
