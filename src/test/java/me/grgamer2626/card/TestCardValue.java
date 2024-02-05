package me.grgamer2626.card;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class TestCardValue {
	
	@Test
	public void testCardValue_Figure2() {
		//Give
		Figures figure = Figures._2;
		int expectedValue = 2;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure3() {
		//Give
		Figures figure = Figures._3;
		int expectedValue = 3;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure4() {
		//Give
		Figures figure = Figures._4;
		int expectedValue = 4;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure5() {
		//Give
		Figures figure = Figures._5;
		int expectedValue = 5;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure6() {
		//Give
		Figures figure = Figures._6;
		int expectedValue = 6;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure7() {
		//Give
		Figures figure = Figures._7;
		int expectedValue = 7;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure8() {
		//Give
		Figures figure = Figures._8;
		int expectedValue = 8;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure9() {
		//Give
		Figures figure = Figures._9;
		int expectedValue = 9;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_Figure10() {
		//Give
		Figures figure = Figures._10;
		int expectedValue = 10;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_FigureJack() {
		//Give
		Figures figure = Figures.JACK;
		int expectedValue = 10;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_FigureQueen() {
		//Give
		Figures figure = Figures.QUEEN;
		int expectedValue = 10;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_FigureKing() {
		//Give
		Figures figure = Figures.KING;
		int expectedValue = 10;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testCardValue_FigureAce() {
		//Give
		Figures figure = Figures.ACE;
		int expectedValue = -1;
		
		//then
		testCardValue(figure, expectedValue);
	}
	
	@Test
	public void testJokerValue() {
		//give
		Card joker = new Joker(1);
		
		//when
		int expectedValue = -1;
		
		//then
		Assert.isTrue(joker.getValue() == expectedValue, "Incorrect joker value!");
	}
	
	private void testCardValue(Figures figure, int expectedValue) {
		//given
		Card card = new StandardCard(1, Colors.HEARTS, figure);
		
		//then
		Assert.isTrue(card.getValue() == expectedValue, "Incorrect figure " + figure.getName() + " card value!");
	}
}
