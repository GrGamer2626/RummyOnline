package me.grgamer2626.service.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.grgamer2626.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailManager implements MailService {
	
	private final JavaMailSender mailSender;
	private final String from;
	
	@Autowired
	public MailManager(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
		this.mailSender = mailSender;
		this.from = from;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public MimeMessage createMimeMessage(String sendTo, String subject, String content) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		
		String senderName = "Game Fan";
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom(from, senderName);
		messageHelper.setTo(sendTo);
		messageHelper.setSubject(subject);
		messageHelper.setText(content, true);
		
		return messageHelper.getMimeMessage();
	}
	
	@Override
	public void sendMail(MimeMessage message) {
		mailSender.send(message);
	}
}
