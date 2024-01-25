package me.grgamer2626.service.users.exceptions.emailValidation;

public class TokenExpiredException extends EmailVerificationException {
	
	public TokenExpiredException() {
	}
	
	public TokenExpiredException(String message) {
		super(message);
	}
}
