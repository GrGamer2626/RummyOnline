package me.grgamer2626.service.users.exceptions.registration;

public abstract class RegistrationException extends Exception { //RunTimeException ??
	
	
	public RegistrationException() {
	}
	
	public RegistrationException(String message) {
		super(message);
	}
}
