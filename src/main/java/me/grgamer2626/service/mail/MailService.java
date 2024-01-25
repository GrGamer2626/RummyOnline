package me.grgamer2626.service.mail;

import jakarta.mail.MessagingException;
import me.grgamer2626.model.users.User;

import java.io.UnsupportedEncodingException;

public interface MailService {
	
	void sendVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException;
	
	String createVerificationUrl(String applicationUrl, String verificationToken);
	
}
