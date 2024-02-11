package me.grgamer2626.service.changePassword.exceptions;

public abstract class ChangePasswordException extends Exception {

    public ChangePasswordException() {
    }

    public ChangePasswordException(String message) {
        super(message);
    }
}
