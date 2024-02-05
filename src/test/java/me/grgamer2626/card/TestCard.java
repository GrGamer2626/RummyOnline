package me.grgamer2626.card;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;


public class TestCard {
	
	///////////////////// Creating Standard Card /////////////////////
	
	@Test
	public void testCard_FigureNull() {
		assertThrows(NullPointerException.class, () ->  new StandardCard(1, Colors.HEARTS, null), "Standard Card figure is null!");
	}
	
	@Test
	public void testCard_ColorNull() {
		assertThrows(NullPointerException.class, () ->  new StandardCard(1, null, Figures.ACE), "Standard Card color is null!");
	}
	
	///////////////////// Is Joker /////////////////////
	
	@Test
	public void testCard_isJoker() {
		//give
		Card card = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		
		//then
		assertFalse(card.isJoker(), "Method isJoker return incorrect value!");
	}
	
	@Test
	public void testJokerCard_isJoker() {
		//give
		Card joker = new Joker(2);
		
		//then
		assertTrue(joker.isJoker(), "Method isJoker return incorrect value!");
	}
	
	///////////////////// Img Path /////////////////////
	
	@Test
	public void testCard_imgPath() {
		//given
		Card card = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		
		//when
		String imgPath = card.getImgPath();
		String expectedImgPath = "/img/deck/hearts/ace.png";
		
		//then
		assertEquals(imgPath, expectedImgPath, "Incorrect card image path!");
	}
	
	@Test
	public void testJokerCard_imgPath() {
		//given
		Card joker = new Joker(1);
		
		//when
		String imgPath = joker.getImgPath();
		String expectedImgPath = "/img/deck/jokers/joker2.png";
		
		//then
		assertEquals(imgPath, expectedImgPath, "Incorrect card image path!");
	}
	
	@Test
	public void testJokerCard_imgPathColored() {
		//given
		Card jokerColored = new Joker(1, true);
		
		//when
		String imgPath = jokerColored.getImgPath();
		String requireImgPath = "/img/deck/jokers/joker1.png";
		
		//then
		Assert.isTrue(imgPath.equals(requireImgPath), "Incorrect card image path!");
	}
	
	

}
