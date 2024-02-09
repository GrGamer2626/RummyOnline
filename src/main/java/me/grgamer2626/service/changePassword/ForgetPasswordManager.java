package me.grgamer2626.service.changePassword;


import me.grgamer2626.model.users.User;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.utils.dto.forgetPassword.ForgetPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ForgetPasswordManager implements ForgetPasswordService {
	
	private final UserService userService;
	
	@Autowired
	public ForgetPasswordManager(UserService userService) {
		this.userService = userService;
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
