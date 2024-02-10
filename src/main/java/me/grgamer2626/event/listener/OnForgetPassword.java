package me.grgamer2626.event.listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import me.grgamer2626.event.ForgetPasswordEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePasswordToken;
import me.grgamer2626.service.mail.MailService;
import me.grgamer2626.service.changePassword.ChangePasswordService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class OnForgetPassword implements ApplicationListener<ForgetPasswordEvent> {
	
	private final ChangePasswordService changePasswordService;
	private final MailService mailService;
	
	public OnForgetPassword(ChangePasswordService changePasswordService, MailService mailService) {
		this.changePasswordService = changePasswordService;
		this.mailService = mailService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onApplicationEvent(ForgetPasswordEvent event) {
		User user = event.getUser();
		String token = changePasswordService.createToken().toString();

		createChangePasswordToken(user, token);

		String url = changePasswordService.createUrl(event.getApplicationUrl(), token);
		
		try {
			MimeMessage message = mailService.createMimeMessage(user.getEmail(), "Change Password", getContent(url));
			mailService.sendMail(message);
			
		}catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private ChangePasswordToken createChangePasswordToken(User user, String token) {
		ChangePasswordToken changePassword = changePasswordService.findByUser(user);

		if(changePassword == null) {

			return changePasswordService.saveChangePasswordToken(user, token);

		}else {
			changePassword.setToken(token);
			changePassword.setExpirationTime(changePassword.getTokenExpirationTime());

			return  changePasswordService.saveChangePasswordToken(changePassword);
		}
	}



	private String getContent(String url) {
		return  "<p>We have received a password reset request for your account.</p>" +
				"<p>To do this, simply click the link below:<br><a href=\"" + url + "\">Change Password</a></p>" +
				"<p>If the link doesn't work, copy the following URL and paste it into your browser's address bar:<br>" + url + " </p>" +
				"<p>The password reset link will expire within 30 minutes of sending this message.</p>" +
				"<p>If you didn't make this request or believe this message was sent in error, please disregard it or contact us.</p>" +
				"<p>Best regards,</p>" +
				"<p>Game Fan</p>";
	}
}
