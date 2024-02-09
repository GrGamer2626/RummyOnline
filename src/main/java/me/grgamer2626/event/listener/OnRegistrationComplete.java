package me.grgamer2626.event.listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.grgamer2626.event.RegistrationCompleteEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.mail.MailService;
import me.grgamer2626.service.token.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


@Component
public class OnRegistrationComplete implements ApplicationListener<RegistrationCompleteEvent> {
	

	private final VerificationTokenService tokenService;
	private final MailService mailService;
	
	@Autowired
	public OnRegistrationComplete(VerificationTokenService tokenService, MailService mailService) {
		this.tokenService = tokenService;
		this.mailService = mailService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		
		String verificationToken = tokenService.createToken().toString();
		
		tokenService.saveVerificationToken(user, verificationToken);
		
		String url = tokenService.createUrl(event.getApplicationUrl(), verificationToken);
		
		try {
			MimeMessage message = mailService.createMimeMessage(user.getEmail(), "Email Verification", getContent(user.getName(), url));
			mailService.sendMail(message);
			
		}catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private String getContent(String userName, String url) {
		return "<p> Hi, " + userName + ", </p>" +
				"<p>Thanks for signing up on our platform! To complete your registration and unlock the full features of our service, we request you to confirm your email address.</p>" +
				"<p>To do this, simply click the link below:<br><a href=\"" + url + "\">Confirm Email</a></p>" +
				"<p>If the link doesn't work, copy the following URL and paste it into your browser's address bar:<br>" + url + " </p>" +
				"<p>Please be aware that this verification link will expire within 10 minutes of your registration.</p>" +
				"<p>Thanks for being a part of our community!</p>" +
				"<p>Best regards,</p>" +
				"<p>Game Fan</p>";
				
	}
}
