package me.grgamer2626.service.websocket;

public interface WebSocketService {
	
	void sendTo(String destination, Object payload);
	
	void sendToUser(String userName, String destination, Object payload);
	
	
}
