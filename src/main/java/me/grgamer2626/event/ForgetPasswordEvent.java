package me.grgamer2626.event;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePasswordToken;
import org.springframework.context.ApplicationEvent;

public class ForgetPasswordEvent extends ApplicationEvent {
	
	private ChangePasswordToken changePasswordToken;
	private String applicationUrl;
	
	
	public ForgetPasswordEvent(ChangePasswordToken changePasswordToken, String applicationUrl) {
		super(changePasswordToken);
		this.changePasswordToken = changePasswordToken;
		this.applicationUrl = applicationUrl;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public ChangePasswordToken getChangePasswordToken() {
		return changePasswordToken;
	}

	public void setChangePasswordToken(ChangePasswordToken changePasswordToken) {
		this.changePasswordToken = changePasswordToken;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}
	
	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}
}
