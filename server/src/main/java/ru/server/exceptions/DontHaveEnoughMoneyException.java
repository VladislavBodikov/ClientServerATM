package ru.server.exceptions;

public class DontHaveEnoughMoneyException extends RuntimeException {

    public DontHaveEnoughMoneyException(String message) {
        super(message);
    }

    public DontHaveEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}
