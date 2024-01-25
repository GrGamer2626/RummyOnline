package me.grgamer2626.service.users.exceptions.emailValidation;

public class EmailAlreadyConfirmedException extends EmailVerificationException {
	
	public EmailAlreadyConfirmedException() {
	}
	
	public EmailAlreadyConfirmedException(String message) {
		super(message);
	}
}
