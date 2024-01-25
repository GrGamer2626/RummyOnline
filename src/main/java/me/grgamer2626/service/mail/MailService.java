package me.grgamer2626.service.mail;

import jakarta.mail.MessagingException;
import me.grgamer2626.model.users.User;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public interface MailService {
	
	void sendVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException;
	
	String createVerificationUrl(String applicationUrl, String verificationToken);
	
	 default UUID generateUUID() {
		 return UUID.randomUUID();
	 }
	
	 default UUID generateUUID(String key, String userEmail) {
		 String tokenBase = key + ":" + userEmail;
		 
		 return UUID.nameUUIDFromBytes(tokenBase.getBytes());
	 }



}
