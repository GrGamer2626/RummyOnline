package me.grgamer2626.model.users.token;

import me.grgamer2626.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationTokenRepository extends JpaRepository <VerificationToken, Long> {
	
	VerificationToken findByToken(String token);
	
	VerificationToken findByUser(User user);
	
}
