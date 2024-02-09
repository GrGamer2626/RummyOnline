package me.grgamer2626.utils;

import java.util.UUID;

public interface TokenGenerator {
	
	default UUID createToken() {
		return UUID.randomUUID();
	}
	
	default UUID createToken(String key, String userEmail) {
		String tokenBase = key + ":" + userEmail;
		return UUID.nameUUIDFromBytes(tokenBase.getBytes());
	}
	
}
