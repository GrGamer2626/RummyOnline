package me.grgamer2626.event.listener;

import jakarta.mail.MessagingException;
import me.grgamer2626.event.RegistrationCompleteEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.mail.MailService;
import me.grgamer2626.service.users.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


@Component
public class OnRegistrationComplete implements ApplicationListener<RegistrationCompleteEvent> {
	
	private final UserRegistrationService registrationService;
	private final MailService mailService;
	
	@Autowired
	public OnRegistrationComplete(UserRegistrationService registrationService, MailService mailService) {
		this.registrationService = registrationService;
		this.mailService = mailService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		
		String verificationToken = mailService.generateUUID().toString();
		
		registrationService.saveUserVerificationToken(user, verificationToken);
		
		String url = mailService.createVerificationUrl(event.getApplicationUrl(), verificationToken);
		try {
			mailService.sendVerificationEmail(user, url);
			
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
