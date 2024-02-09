package me.grgamer2626.model.users.changePassword;

import me.grgamer2626.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangePasswordRepository extends JpaRepository<ChangePassword, Long> {
	
	ChangePassword findByToken(String ticket);
	
	ChangePassword findByUser(User user);
}
