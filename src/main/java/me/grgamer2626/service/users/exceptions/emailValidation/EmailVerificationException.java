package me.grgamer2626.service.users.exceptions.emailValidation;

public abstract class EmailVerificationException extends Exception {
	
	public EmailVerificationException() {
	}
	
	public EmailVerificationException(String message) {
		super(message);
	}
}
