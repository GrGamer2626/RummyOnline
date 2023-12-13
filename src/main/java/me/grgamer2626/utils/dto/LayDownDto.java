package me.grgamer2626.utils.dto;

import java.util.List;
import java.util.Map;

public class LayDownDto {
	
	private final int playerSlot;
	private final Map<Integer, List<CardDto>> sequences;
	
	public LayDownDto(int playerSlot, Map<Integer, List<CardDto>> sequences) {
		this.playerSlot = playerSlot;
		this.sequences = sequences;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getPlayerSlot() {
		return playerSlot;
	}
	
	public Map<Integer, List<CardDto>> getSequences() {
		return sequences;
	}
}
