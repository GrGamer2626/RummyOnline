package me.grgamer2626.service.users.registration;

import jakarta.servlet.http.HttpServletRequest;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.token.VerificationToken;
import me.grgamer2626.service.users.exceptions.emailValidation.EmailVerificationException;
import me.grgamer2626.service.users.exceptions.registration.RegistrationException;
import me.grgamer2626.utils.dto.UserRegistrationDto;
import org.springframework.validation.BindingResult;


public interface UserRegistrationService {
	
	User registerUser(UserRegistrationDto registrationDto) throws RegistrationException;
	
	void validateUser(UserRegistrationDto dto, BindingResult bindingResult);
	
	User validateEmail(VerificationToken token) throws EmailVerificationException;
	
	String createApplicationUrl(HttpServletRequest request);
}
