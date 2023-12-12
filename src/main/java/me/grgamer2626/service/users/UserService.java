package me.grgamer2626.service.users;

import me.grgamer2626.model.users.User;
import me.grgamer2626.utils.dto.UserRegistrationDto;
import org.springframework.validation.BindingResult;


public interface UserService {
	
	User registerUser(UserRegistrationDto registrationDto);
	
	
	void validateUser(UserRegistrationDto dto, BindingResult bindingResult);
	
	User findByName(String name);
	
	User findByEmail(String email);
}
