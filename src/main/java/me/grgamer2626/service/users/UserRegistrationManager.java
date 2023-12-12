package me.grgamer2626.service.users;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.UserRepository;
import me.grgamer2626.model.users.roles.Role;
import me.grgamer2626.model.users.roles.RoleRepository;
import me.grgamer2626.model.users.roles.RoleType;
import me.grgamer2626.utils.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@SuppressWarnings("unused")
@Service()
public class UserRegistrationManager implements UserRegistrationService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserRegistrationManager(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public User registerUser(UserRegistrationDto dto) {
		String nickName =  dto.getNickName();
		String email = dto.getEmail();
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		
		Role userRole = roleRepository.findByName(RoleType.USER);
		
		User user = new User(nickName, email,encodedPassword, List.of(userRole));

		return userRepository.save(user);
	}
	
	@Override
	public void validateUser(UserRegistrationDto dto, BindingResult bindingResult) {
		if(isNameTaken(dto.getNickName())) bindingResult.rejectValue("nickName", "error.nickNameTaken", "This name is already taken!");
		if(isEmailRegistered(dto.getEmail())) bindingResult.rejectValue("email", "error.emailTaken", "This email is already registered!");
	}
	
	@Override
	public User findByName(String name) {
		return userRepository.findByName(name);
	}
	
	private boolean isNameTaken(String nickName) {
		return findByName(nickName) != null;
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	private boolean isEmailRegistered(String email) {
		return findByEmail(email) != null;
	}
}
