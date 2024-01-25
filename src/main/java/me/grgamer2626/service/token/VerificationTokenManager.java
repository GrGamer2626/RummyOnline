package me.grgamer2626.service.token;

import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.token.VerificationToken;
import me.grgamer2626.model.users.token.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenManager implements VerificationTokenService {

	private final VerificationTokenRepository tokenRepository;
	
	@Autowired
	public VerificationTokenManager(VerificationTokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public VerificationToken saveVerificationToken(User user, String token) {
		VerificationToken verificationToken = new VerificationToken(user, token);
		
		return tokenRepository.save(verificationToken);
	}
	
	@Override
	public UUID createToken() {
		return UUID.randomUUID();
	}
	
	@Override
	public UUID createToken(String key, String userEmail) {
		String tokenBase = key + ":" + userEmail;
		return UUID.nameUUIDFromBytes(tokenBase.getBytes());
	}
	
	@Override
	public VerificationToken findByToken(String token) {
		return tokenRepository.findByToken(token);
	}
	
	@Override
	public VerificationToken findByUser(User user) {
		return tokenRepository.findByUser(user);
	}
	
	@Override
	public void deleteVerificationToken(VerificationToken token) {
		tokenRepository.delete(token);
	}
}
