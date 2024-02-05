package me.grgamer2626;

import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.tables.PlayerSlots;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
public class TestPlayerSlots {
	
	@Test
	public void testPlayerSlots_Initialize() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		//then
		assertEquals(6, playerSlots.size(), "There should be 6 player slots!");
		assertTrue(playerSlots.values().stream().allMatch(Objects::isNull), "Whole slots should be null after created!");
	}
	
	@Test
	public void putPlayer_InRange() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		//when
		int slot = 3;
		Player player = new Player(1, "Tester", slot);
		
		//then
		assertDoesNotThrow(()-> playerSlots.put(slot, player), "Should not throw IllegalArgumentException!");
		assertEquals(player, playerSlots.get(slot), "Slot does not contains player!");
	}
	
	@Test
	public void putPlayer_OutOfRange() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		//when
		int slot = 7;
		Player player = new Player(1, "Tester", slot);
		
		//then
		assertThrows(IllegalArgumentException.class, ()-> playerSlots.put(slot, player), "Added a new key should not be possible!");
	}
	
	@Test
	public void putAllPlayer_3Slots() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		Map<Integer, Player> map = new HashMap<>();
		
		int slot1 = 1;
		int slot2 = 2;
		int slot3 = 3;
		
		Player player1 = new Player(1, "Tester1", slot1);
		Player player2 = new Player(2, "Tester2", slot2);
		Player player3 = new Player(3, "Tester3", slot3);
		
		map.put(slot1, player1);
		map.put(slot2, player2);
		map.put(slot3, player3);
		
		//when
		playerSlots.putAll(map);
		
		boolean test =
				playerSlots.get(1).equals(player1) &&
				playerSlots.get(2).equals(player2) &&
				playerSlots.get(3).equals(player3);
		
		//then
		assertTrue(test, "Some values were not added!");
	}
	
	@Test
	public void putAllPlayer_6Slots() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		Map<Integer, Player> map = new HashMap<>();
		
		int slot1 = 1;
		int slot2 = 2;
		int slot3 = 3;
		int slot4 = 4;
		int slot5 = 5;
		int slot6 = 6;
		
		Player player1 = new Player(1, "Tester1", slot1);
		Player player2 = new Player(2, "Tester2", slot2);
		Player player3 = new Player(3, "Tester3", slot3);
		Player player4 = new Player(4, "Tester4", slot4);
		Player player5 = new Player(5, "Tester5", slot5);
		Player player6 = new Player(6, "Tester6", slot6);
		
		map.put(slot1, player1);
		map.put(slot2, player2);
		map.put(slot3, player3);
		map.put(slot4, player4);
		map.put(slot5, player5);
		map.put(slot6, player6);
		
		
		//when
		playerSlots.putAll(map);
		
		boolean test =
				playerSlots.get(1).equals(player1) &&
				playerSlots.get(2).equals(player2) &&
				playerSlots.get(3).equals(player3) &&
				playerSlots.get(4).equals(player4) &&
				playerSlots.get(5).equals(player5) &&
				playerSlots.get(6).equals(player6);
		
		//then
		assertTrue(test, "Some values were not added!");
	}
	
	@Test
	public void putAllPlayer_outOfSize() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		Map<Integer, Player> map = new HashMap<>();
		
		//when
		int slot1 = 1;
		int slot2 = 2;
		int slot3 = 3;
		int slot4 = 4;
		int slot5 = 5;
		int slot6 = 6;
		int slot7 = 7;
		
		Player player1 = map.put(slot1, new Player(1, "Tester1", slot1));
		Player player2 = map.put(slot2, new Player(2, "Tester2", slot2));
		Player player3 = map.put(slot3, new Player(3, "Tester3", slot3));
		Player player4 = map.put(slot4, new Player(4, "Tester4", slot4));
		Player player5 = map.put(slot5, new Player(5, "Tester5", slot5));
		Player player6 = map.put(slot6, new Player(6, "Tester6", slot6));
		Player player7 = map.put(slot7, new Player(7, "Tester7", slot7));
		
		//then
		assertThrows(IllegalArgumentException.class, ()-> playerSlots.putAll(map), "Added map out of size!");
	}
	@Test
	public void putAllPlayer_KeyOutOfRange() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		Map<Integer, Player> map = new HashMap<>();
		
		//when
		int slot1 = 1;
		int slot2 = 2;
		int outOfRange = 7;
		
		map.put(slot1, new Player(1, "Tester1", slot1));
		map.put(slot2, new Player(2, "Tester2", slot2));
		map.put(outOfRange, new Player(3, "Tester3", outOfRange));
		
		//then
		assertThrows(IllegalArgumentException.class, ()-> playerSlots.putAll(map), "Added illegal key to player slots!");
	}
	
	@Test
	public void removePlayer_correctKey() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot = 1;
		Player player = new Player(1, "Tester", slot);
		playerSlots.put(slot, player);
		
		//when
		playerSlots.remove(slot);
		
		//then
		assertTrue(playerSlots.containsKey(slot), "The key should not be removed!");
		assertNull(playerSlots.get(1), "The player should be removed from the slot!");
	}
	
	@Test
	public void removePlayer_incorrectKey() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		//when
		int slot = 7;
		
		//then
		assertThrows(IllegalArgumentException.class, ()-> playerSlots.remove(slot), "Value removed for a key that does not exist! Expected IllegalArgumentException!");
	}
	
	@Test
	public void removePlayer_keyMatchValue() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		int slot = 1;
		Player player = new Player(1, "Tester", slot);
		
		//when
		playerSlots.put(slot, player);
		
		//then
		assertTrue(playerSlots.remove(slot, player),  "Expected value true!");
		assertNull(playerSlots.get(slot), "Player should be removed!");
	}
	
	@Test
	public void removePlayer_keyNotMatchValue() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		int slot = 1;
		Player player = new Player(1, "Tester", slot);
		
		//when
		playerSlots.put(slot, player);
		
		//then
		assertFalse(playerSlots.remove(2, player), "Expected UnsupportedOperationException!");
		assertTrue(playerSlots.containsValue(player), "Player slots could contains player!");
	}
	
	@Test
	public void removePlayer_IllegalKey() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		int slot = 1;
		Player player = new Player(1, "Tester", slot);
		
		//when
		playerSlots.put(slot, player);
		
		//then
		assertThrows(IllegalArgumentException.class, ()-> playerSlots.remove(7, player), "Expected IllegalArgumentException");
	}
	
	@Test
	public void getNotNull_EmptyPlayerSlots() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		//then
		assertTrue(playerSlots.getNonNull().isEmpty(), "List should be empty!");
	}
	
	@Test
	public void getNotNull_NotEmptyPlayerSlots() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		int slot = 1;
		Player player1 = new Player(1, "Tester1", slot);
		playerSlots.put(slot, player1);
		
		//when
		List<Player> notNull = playerSlots.getNonNull();
		
		//then
		assertEquals(1, notNull.size(), "List should not be empty!");
		assertTrue(notNull.contains(player1), "List does not contain player!");
	}
	
	@Test
	public void getByName_correctName() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		String name = "Tester1";
		Player player1 = new Player(1, name, slot1);
		
		playerSlots.put(slot1, player1);
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertEquals(player1, playerSlots.getByName(name), "Player should be founded!");
	}
	
	@Test
	public void getByName_incorrectName() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertNull(playerSlots.getByName("jpiigmdinpk"), "Player should be null!");
	}
	
	@Test
	public void getById_correctId() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();

		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		
		playerSlots.put(slot1, player1);
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertEquals(player1, playerSlots.getById(1), "Player should be founded!");
	}
	
	@Test
	public void getById_incorrectId() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertNull(playerSlots.getById(7), "Player should be null!");
	}
	
	@Test
	public void occupiedAnyKey_correctPlayer() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		
		playerSlots.put(slot1, player1);
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertTrue(playerSlots.occupiedAnyKey(player1), "");
	}
	
	@Test
	public void occupiedAnyKey_incorrectPlayer() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player2 = new Player(2, "Tester2", slot2);
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		
		//then
		assertFalse(playerSlots.occupiedAnyKey(player2), "");
	}
	
	@Test
	public void occupiedAnyKey_correctPlayerName() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		
		playerSlots.put(slot1, player1);
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertTrue(playerSlots.occupiedAnyKey(player1.getName()), "");
	}
	
	@Test
	public void occupiedAnyKey_incorrectPlayerName() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player2 = new Player(2, "Tester2", slot2);
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		
		//then
		assertFalse(playerSlots.occupiedAnyKey(player2.getName()), "Player slots should not contain this player!");
	}
	
	@Test
	public void getKey_correctPlayer() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		
		playerSlots.put(slot1, player1);
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertEquals(slot1, playerSlots.getKey(player1), "Incorrect key!");
	}
	
	@Test
	public void getKey_incorrectPlayer() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		
		Player player2 = new Player(2, "Tester2", slot2);
		
		//then
		assertEquals(-1, playerSlots.getKey(player2), "Incorrect key!");
	}
	
	@Test
	public void isKeyTaken_keyTaken() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		Player player2 = new Player(2, "Tester2", slot2);
		
		playerSlots.put(slot1, player1);
		playerSlots.put(slot2, player2);
		
		//then
		assertTrue(playerSlots.isKeyTaken(slot1), "Slot should be taken!");
	}
	
	@Test
	public void isKeyTaken_keyNotTaken() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertFalse(playerSlots.isKeyTaken(6), "Slot should be not taken!");
	}
	
	@Test
	public void getPlayerAmount() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertEquals(2, playerSlots.getPlayerAmount(), "Incorrect player amount!");
	}
	
	@Test
	public void getReadyPlayersCount_NoReadyPlayers() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		playerSlots.put(slot1, new Player(1, "Tester1", slot1));
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertEquals(0, playerSlots.readyPlayersCount());
	}
	
	@Test
	public void getReadyPlayersCount_OneReadyPlayer() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		playerSlots.put(slot1, player1);
		player1.setPushedStart(true);
		
		playerSlots.put(slot2, new Player(2, "Tester2", slot2));
		
		//then
		assertEquals(1, playerSlots.readyPlayersCount());
	}
	
	@Test
	public void getReadyPlayersCount_AllReadyPlayers() {
		//give
		PlayerSlots playerSlots = new PlayerSlots();
		
		int slot1 = 1;
		int slot2 = 2;
		
		Player player1 = new Player(1, "Tester1", slot1);
		playerSlots.put(slot1, player1);
		player1.setPushedStart(true);
		
		Player player2 = new Player(2, "Tester2", slot2);
		playerSlots.put(slot2, player2);
		player2.setPushedStart(true);
		
		//then
		assertEquals(2, playerSlots.readyPlayersCount());
	}
}
