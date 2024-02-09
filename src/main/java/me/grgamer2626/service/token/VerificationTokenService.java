package me.grgamer2626.service.token;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.token.VerificationToken;
import me.grgamer2626.utils.TokenGenerator;

import java.util.UUID;

public interface VerificationTokenService extends TokenGenerator {
	
	VerificationToken saveVerificationToken(User user, String token);
	
	VerificationToken findByToken(String token);
	
	VerificationToken findByUser(User user);
	
	void deleteVerificationToken(VerificationToken token);
	
	String createUrl(String applicationUrl, String verificationToken);
}
