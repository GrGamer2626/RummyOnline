package me.grgamer2626.service.changePassword;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePassword;
import me.grgamer2626.model.users.changePassword.ChangePasswordRepository;
import me.grgamer2626.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordManager implements ChangePasswordService {
	
	private final UserService userService;
	private final ChangePasswordRepository changePasswordRepository;
	private final PasswordEncoder encoder;
	
	@Autowired
	public ChangePasswordManager(UserService userService, ChangePasswordRepository changePasswordRepository, PasswordEncoder encoder) {
		this.userService = userService;
		this.changePasswordRepository = changePasswordRepository;
		this.encoder = encoder;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public ChangePassword saveChangePasswordToken(User user, String token) {
		ChangePassword changePassword = new ChangePassword(user, token);
		
		return changePasswordRepository.save(changePassword);
	}
	
	@Override
	public ChangePassword findByToken(String token) {
		return changePasswordRepository.findByToken(token);
	}
	
	@Override
	public ChangePassword findByUser(User user) {
		return changePasswordRepository.findByUser(user);
	}
	
	@Override
	public void deleteChangePasswordToken(ChangePassword changePassword) {
		changePasswordRepository.delete(changePassword);
	}
	
	@Override
	public String createUrl(String applicationUrl, String changePasswordToken) {
		return applicationUrl + "/change-password?token=" + changePasswordToken;
	}
	
	@Override
	public User changePassword(User user, String newPassword) {
		String encodedPassword = encoder.encode(newPassword);
		
		user.setPassword(encodedPassword);
		
		return userService.save(user);
	}
	
	
}
