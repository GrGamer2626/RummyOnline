package me.grgamer2626.event.listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.grgamer2626.event.RegistrationCompleteEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.users.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Component
public class OnRegistrationComplete implements ApplicationListener<RegistrationCompleteEvent> {
	
	private final UserRegistrationService registrationService;
	private final JavaMailSender mailSender;
	private final String from;
	
	@Autowired
	public OnRegistrationComplete(UserRegistrationService registrationService, JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
		this.registrationService = registrationService;
		this.mailSender = mailSender;
		this.from = from;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		String verificationToken = generateUUID("RummyOnline", user.getEmail()).toString();
		registrationService.saveUserVerificationToken(user, verificationToken);
		
		String url = event.getApplicationUrl() + "/registration/emailVerification?token=" + verificationToken;
		try {
			sendVerificationEmail(user, url);
			
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private UUID generateUUID(String key, String userEmail) {
		String tokenBase = key + ":" + userEmail;
		
		return UUID.nameUUIDFromBytes(tokenBase.getBytes());
	}
	
	private void sendVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException {
		String subject = "Email Verification";
		String senderName = "Game Fan";
		
		String content = "<p> Hi, " + user.getName() + ", </p>" +
						 "<p>Thanks for signing up on our platform! To complete your registration and unlock the full features of our service, we request you to confirm your email address.</p>" +
						 "<p>To do this, simply click the link below:<br><a href=\"" + url + "\">Confirm Email</a></p>" +
						 "<p>If the link doesn't work, copy the following URL and paste it into your browser's address bar:<br>" + url + " </p>" +
						 "<p>Please be aware that this verification link will expire within 10 minutes of your registration.</p>" +
						 "<p>Thanks for being a part of our community!</p>" +
						 "<p>Best regards,</p>" +
						 "<p>Game Fan</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom(from, senderName);
		messageHelper.setTo(user.getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(content, true);
		
		mailSender.send(messageHelper.getMimeMessage());
	}
}
