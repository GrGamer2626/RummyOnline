package me.grgamer2626.model.tables;

import me.grgamer2626.model.games.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class PlayerSlots extends HashMap<Integer, Player> {
	
	private static final int FIXED_SIZE = 6;
	
	public PlayerSlots() {
		initializeMap();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void initializeMap() {
		for(int i = 1; i <= FIXED_SIZE; i++) {
			super.put(i, null);
		}
	}
	
	
	@Override
	public Player put(Integer key, Player value) {
		if (containsKey(key)) {
			return super.put(key, value);
			
		}else throw new IllegalArgumentException("Adding new keys is not allowed");
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Player> map) {
		if(map.size() > FIXED_SIZE || !keySet().containsAll(map.keySet())) {
			throw new IllegalArgumentException("Map contains illegals key value! The player slots must contain values between 1 and 6.");
		}
		super.putAll(map);
	}
	
	@Override
	public Player remove(Object key) {
		if (containsKey(key)) {
			return super.put((Integer) key, null);
			
		} else throw new IllegalArgumentException("The player slots does not contain the key: " + key);
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		if(!containsKey(key)) throw new IllegalArgumentException("The player slots does not contain the key: " + key);
		Player player = get(key);
		
		if(player != null && player.equals(value)) {
			super.put((Integer) key, null);
			return true;
		}
		return false;
	}
	
	public List<Player> getNonNull() {
		return values().stream()
				.filter(Objects::nonNull)
				.toList();
	}
	
	public Player getByName(String playerName) {
		return getNonNull().stream()
				.filter(player -> player.getName().equalsIgnoreCase(playerName))
				.findFirst()
				.orElse(null);
	}
	
	public Player getById(long playerId) {
		return getNonNull().stream()
				.filter(player -> player.getId() == playerId)
				.findFirst()
				.orElse(null);
	}
	public boolean occupiedAnyKey(Player player) {
		return getNonNull().stream()
				.anyMatch(p-> p.equals(player));
	}
	public boolean occupiedAnyKey(String playerName) {
		return getNonNull().stream()
				.map(Player::getName)
				.anyMatch(name-> name.equalsIgnoreCase(playerName));
	}
	
	public Integer getKey(Player player) {
		return entrySet()
				.stream()
				.filter(entry -> player.equals(entry.getValue()))
				.map(Entry::getKey)
				.findFirst()
				.orElse(-1);
	}
	
	public boolean isKeyTaken(Integer slot) {
		return get(slot) != null;
	}
	
	public int getPlayerAmount() {
		return getNonNull().size();
	}
	
	public long readyPlayersCount() {
		return getNonNull().stream()
				.filter(Player::isPushedStart)
				.count();
	}
}
