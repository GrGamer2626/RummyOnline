package me.grgamer2626.event;

import me.grgamer2626.model.users.User;
import org.springframework.context.ApplicationEvent;

public class ForgetPasswordEvent extends ApplicationEvent {
	
	private User user;
	private String applicationUrl;
	
	
	public ForgetPasswordEvent(User user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getApplicationUrl() {
		return applicationUrl;
	}
	
	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}
}
