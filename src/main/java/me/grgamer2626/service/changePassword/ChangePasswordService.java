package me.grgamer2626.service.changePassword;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePassword;
import me.grgamer2626.utils.TokenGenerator;

public interface ChangePasswordService extends TokenGenerator {
	
	ChangePassword saveChangePasswordToken(User user, String token);
	
	ChangePassword findByToken(String token);
	
	ChangePassword findByUser(User user);
	
	void deleteChangePasswordToken(ChangePassword changePassword);
	
	String createUrl(String applicationUrl, String changePasswordToken);
	
	User changePassword(User user, String newPassword);
}
