package me.grgamer2626.service.users.exceptions.emailValidation;

public class InvalidTokenException extends EmailVerificationException {
	
	public InvalidTokenException() {
	}
	
	public InvalidTokenException(String message) {
		super(message);
	}
}
