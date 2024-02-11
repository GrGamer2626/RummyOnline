package me.grgamer2626.service.changePassword;


import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePasswordToken;
import me.grgamer2626.model.users.changePassword.ChangePasswordTokenRepository;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.utils.dto.forgetPassword.ForgetPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ForgetPasswordManager implements ForgetPasswordService {
	
	private final UserService userService;
	private final ChangePasswordTokenRepository passwordTokenRepository;
	
	@Autowired
	public ForgetPasswordManager(UserService userService, ChangePasswordTokenRepository passwordTokenRepository) {
		this.userService = userService;
        this.passwordTokenRepository = passwordTokenRepository;
    }

	@Override
	public ChangePasswordToken getChangePasswordToken(User user) {
		ChangePasswordToken changePassword = passwordTokenRepository.findByUser(user);
		String token = createToken().toString();

		if(changePassword == null) {
			return passwordTokenRepository.save(new ChangePasswordToken(user, token));

		}else {
			changePassword.setToken(token);
			changePassword.setExpirationTime(changePassword.getTokenExpirationTime());

			return  passwordTokenRepository.save(changePassword);
		}
	}
	
	@Override
	public void validateDto(ForgetPasswordDto dto, BindingResult bindingResult) {
		if(!userService.isEmailRegistered(dto.getEmail())) {
			bindingResult.rejectValue("email", "error.emailNotExist", "Provide email has been never registered!");
		}
	}
	
	@Override
	public User findByEmail(String email) {
		return userService.findByEmail(email);
	}
}
