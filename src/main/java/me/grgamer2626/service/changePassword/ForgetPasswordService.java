package me.grgamer2626.service.changePassword;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePasswordToken;
import me.grgamer2626.utils.ApplicationUrl;
import me.grgamer2626.utils.TokenGenerator;
import me.grgamer2626.utils.dto.forgetPassword.ForgetPasswordDto;
import org.springframework.validation.BindingResult;

public interface ForgetPasswordService extends ApplicationUrl, TokenGenerator {

	ChangePasswordToken getChangePasswordToken(User user);

	void validateDto(ForgetPasswordDto dto, BindingResult bindingResult);

	User findByEmail(String email);
}
