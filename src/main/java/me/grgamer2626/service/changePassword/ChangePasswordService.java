package me.grgamer2626.service.changePassword;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePasswordToken;
import me.grgamer2626.service.changePassword.exceptions.InvalidTokenException;
import me.grgamer2626.utils.TokenGenerator;

public interface ChangePasswordService extends TokenGenerator {

	
	ChangePasswordToken findByToken(String token);
	
	ChangePasswordToken findByUser(User user);
	
	void deleteChangePasswordToken(ChangePasswordToken changePassword);

	User changePassword(ChangePasswordToken changePasswordToken, String newPassword) throws InvalidTokenException;
}
