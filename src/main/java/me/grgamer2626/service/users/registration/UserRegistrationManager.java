package me.grgamer2626.service.users.registration;

import jakarta.servlet.http.HttpServletRequest;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.roles.Role;
import me.grgamer2626.model.users.roles.RoleRepository;
import me.grgamer2626.model.users.roles.RoleType;
import me.grgamer2626.model.users.token.VerificationToken;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.service.users.exceptions.emailValidation.EmailAlreadyConfirmedException;
import me.grgamer2626.service.users.exceptions.emailValidation.EmailVerificationException;
import me.grgamer2626.service.users.exceptions.emailValidation.InvalidTokenException;
import me.grgamer2626.service.users.exceptions.emailValidation.TokenExpiredException;
import me.grgamer2626.service.users.exceptions.registration.EmailAlreadyExistsException;
import me.grgamer2626.service.users.exceptions.registration.RegistrationException;
import me.grgamer2626.service.users.exceptions.registration.UserNameAlreadyExistsException;
import me.grgamer2626.utils.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserRegistrationManager implements UserRegistrationService {
	
	private final UserService userService;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public UserRegistrationManager(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public User registerUser(UserRegistrationDto dto) throws RegistrationException {
		String email = dto.getEmail();
		if(userService.isEmailRegistered(email)) throw new EmailAlreadyExistsException("User with email " + email + " already exists!");
		
		String nickName =  dto.getNickName();
		if(userService.isNameTaken(nickName)) throw new UserNameAlreadyExistsException("User with nick name " + nickName + " already exists!");
		
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		
		Role userRole = roleRepository.findByName(RoleType.USER);
		if(userRole == null) userRole = new Role(RoleType.USER);
		
		User user = new User(nickName, email, encodedPassword, List.of(userRole));

		return userService.save(user);
	}
	
	@Override
	public void validateUser(UserRegistrationDto dto, BindingResult bindingResult) {
		if(userService.isNameTaken(dto.getNickName())) bindingResult.rejectValue("nickName", "error.nickNameTaken", "This name is already taken!");
		if(userService.isEmailRegistered(dto.getEmail())) bindingResult.rejectValue("email", "error.emailTaken", "This email is already registered!");
	}
	
	@Override
	public User validateEmail(VerificationToken token) throws EmailVerificationException {
		if(token == null) throw new InvalidTokenException("Invalid token!");
		
		User user = token.getUser();
		if(user.isEnabled()) throw new EmailAlreadyConfirmedException("This Email is already confirmed!");
		
		if(token.isExpired()) throw new TokenExpiredException("The token has been expired!");
		
		user.setEnabled(true);
		
		return userService.save(user);
	}
	
	@Override
	public String createApplicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
