package me.grgamer2626.service.users.exceptions.registration;

public class UserNameAlreadyExistsException extends RegistrationException {
	
	public UserNameAlreadyExistsException() {
	}
	
	public UserNameAlreadyExistsException(String message) {
		super(message);
	}
}
