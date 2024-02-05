package me.grgamer2626.sequence;

import me.grgamer2626.model.games.cards.Card;
import me.grgamer2626.model.games.cards.Joker;
import me.grgamer2626.model.games.cards.StandardCard;
import me.grgamer2626.model.games.cards.utils.Colors;
import me.grgamer2626.model.games.cards.utils.Figures;
import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.games.player.sequences.Sequence;

public class TestSequence {
	
	static Sequence createCardSet(Figures figures) {
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card hearts = new StandardCard(1, Colors.HEARTS, figures);
		Card spades = new StandardCard(2, Colors.SPADES, figures);
		Card clubs = new StandardCard(3, Colors.CLUBS, figures);
		
		sequence.add(hearts);
		sequence.add(spades);
		sequence.add(clubs);
		
		return sequence;
	}
	
	static Sequence createAce_2_3Sequence() {
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card cardAce = new StandardCard(1, Colors.HEARTS, Figures.ACE);
		Card card2 = new StandardCard(2, Colors.HEARTS, Figures._2);
		Card card3 = new StandardCard(3, Colors.HEARTS, Figures._3);
		
		sequence.add(cardAce);
		sequence.add(card2);
		sequence.add(card3);
		
		return sequence;
	}
	
	static Sequence createQueenKingAceSequence() {
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card cardQueen = new StandardCard(1, Colors.HEARTS, Figures.QUEEN);
		Card cardKing = new StandardCard(2, Colors.HEARTS, Figures.KING);
		Card cardAce = new StandardCard(3, Colors.HEARTS, Figures.ACE);
		
		sequence.add(cardQueen);
		sequence.add(cardKing);
		sequence.add(cardAce);
		
		return sequence;
	}
	
	static Sequence createUnclearSequence() {
		Player player = new Player(1, "Tester", 1);
		Sequence sequence = new Sequence(1, player);
		
		Card card4 = new StandardCard(1, Colors.HEARTS, Figures._4);
		Joker joker = new Joker(2);
		joker.setFigure(Figures._5);
		Card card6 = new StandardCard(3, Colors.HEARTS, Figures._6);
		
		sequence.add(card4);
		sequence.add(joker);
		sequence.add(card6);
		
		return sequence;
	}
	
}
