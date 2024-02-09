package me.grgamer2626.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.grgamer2626.model.users.User;

import java.io.UnsupportedEncodingException;

public interface MailService {
	
	MimeMessage createMimeMessage(String sendTo, String subject, String content) throws MessagingException, UnsupportedEncodingException;
	
	void sendMail(MimeMessage message);
	
}
