package ru.server.exceptions;

public class SendMoneyToSelfCardException extends RuntimeException{

    public SendMoneyToSelfCardException(String message) {
        super(message);
    }

    public SendMoneyToSelfCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
