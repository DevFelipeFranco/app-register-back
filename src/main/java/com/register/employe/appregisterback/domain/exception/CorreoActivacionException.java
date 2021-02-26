package com.register.employe.appregisterback.domain.exception;

public class CorreoActivacionException extends Exception {

    private static final long serialVersionUID = 2581335119466776504L;

    public CorreoActivacionException(String message, Exception exception) {
        super(message, exception);
    }
}
