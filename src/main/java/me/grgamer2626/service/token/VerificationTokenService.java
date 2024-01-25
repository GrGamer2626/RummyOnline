package me.grgamer2626.service.token;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.token.VerificationToken;

import java.util.UUID;

public interface VerificationTokenService {

	VerificationToken saveVerificationToken(User user, String token);
	
	UUID createToken();
	
	UUID createToken(String key, String userEmail);
	
	VerificationToken findByToken(String token);
	
	VerificationToken findByUser(User user);
	
	void deleteVerificationToken(VerificationToken token);

}
