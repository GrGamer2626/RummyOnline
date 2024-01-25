package me.grgamer2626.service.users.exceptions.registration;

public class EmailAlreadyExistsException extends RegistrationException {
	
	public EmailAlreadyExistsException() {
	}
	
	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
