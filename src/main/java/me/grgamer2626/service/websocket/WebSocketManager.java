package me.grgamer2626.service.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketManager implements WebSocketService {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	public WebSocketManager(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void sendTo(String destination, Object payload) {
		messagingTemplate.convertAndSend(destination, payload);
	}
	
	@Override
	public void sendToUser(String userName, String destination, Object payload) {
		messagingTemplate.convertAndSendToUser(userName, destination, payload);
	}
}
