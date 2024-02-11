package me.grgamer2626.service.changePassword;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePasswordToken;
import me.grgamer2626.model.users.changePassword.ChangePasswordTokenRepository;
import me.grgamer2626.service.changePassword.exceptions.InvalidTokenException;
import me.grgamer2626.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordManager implements ChangePasswordService {
	
	private final UserService userService;
	private final ChangePasswordTokenRepository changePasswordRepository;
	private final PasswordEncoder encoder;
	
	@Autowired
	public ChangePasswordManager(UserService userService, ChangePasswordTokenRepository changePasswordRepository, PasswordEncoder encoder) {
		this.userService = userService;
		this.changePasswordRepository = changePasswordRepository;
		this.encoder = encoder;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public ChangePasswordToken findByToken(String token) {
		return changePasswordRepository.findByToken(token);
	}
	
	@Override
	public ChangePasswordToken findByUser(User user) {
		return changePasswordRepository.findByUser(user);
	}
	
	@Override
	public void deleteChangePasswordToken(ChangePasswordToken changePassword) {
		changePasswordRepository.delete(changePassword);
	}
	
	@Override
	public User changePassword(ChangePasswordToken changePasswordToken, String newPassword) throws InvalidTokenException {
		if(changePasswordToken == null) throw new InvalidTokenException("Invalid token!");
		String encodedPassword = encoder.encode(newPassword);

		User user = changePasswordToken.getUser();
		user.setPassword(encodedPassword);
		
		return userService.save(user);
	}
	
	
}
