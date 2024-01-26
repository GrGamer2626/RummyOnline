package me.grgamer2626.service.users;

import me.grgamer2626.model.users.User;

public interface UserService {
	
	User save(User user);
	
	User findById(Long userId);
	
	User findByName(String name);
	
	User findByEmail(String email);
	
	boolean isNameTaken(String nickName);
	
	boolean isEmailRegistered(String email);
	
	void deleteUser(User user);
}
