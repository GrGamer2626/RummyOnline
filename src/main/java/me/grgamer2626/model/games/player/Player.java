package me.grgamer2626.model.games.player;

public class Player {
	
	
	private final long id;
	private final String name;
	private final int slot;
	
	
	
	
	
	private boolean pushedStart;
	
	
	public Player(long id, String name, int slot) {
		this.id = id;
		this.name = name;
		this.slot = slot;
	}
	
	/**
	 * Returns id number of the player. The id of the player is identical with id of the user.
	 *
	 * @return id of the player
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Returns nickname of the player. The nickname of the player is identical with nickname of the user.
	 *
	 * @return nickname of the player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the slot number taken by this player.
	 *
	 * @return slot number
	 */
	public int getSlot() {
		return slot;
	}
	
	
	public boolean isPushedStart() {
		return pushedStart;
	}
	
	public void setPushedStart(boolean pushedStart) {
		this.pushedStart = pushedStart;
	}
}
