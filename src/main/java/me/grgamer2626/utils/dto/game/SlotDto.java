package me.grgamer2626.utils.dto.game;

public class SlotDto {
	
	private int slot;
	private String playerName;
	
	public SlotDto(int slot, String playerName) {
		this.slot = slot;
		this.playerName = playerName;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
