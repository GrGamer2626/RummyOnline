package me.grgamer2626.model.users.changePassword;

import me.grgamer2626.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangePasswordTokenRepository extends JpaRepository<ChangePasswordToken, Long> {
	
	ChangePasswordToken findByToken(String ticket);
	
	ChangePasswordToken findByUser(User user);
}
