package me.grgamer2626.service.changePassword.exceptions;

public class InvalidTokenException extends ChangePasswordException {

    public InvalidTokenException() {
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
