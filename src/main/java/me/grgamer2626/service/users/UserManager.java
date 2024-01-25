package me.grgamer2626.service.users;


import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManager implements UserService {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserManager(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public User findByName(String name) {
		return userRepository.findByName(name);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public boolean isNameTaken(String nickName) {
		return findByName(nickName) != null;
	}
	
	@Override
	public boolean isEmailRegistered(String email) {
		return findByEmail(email) != null;
	}
	
	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
}
