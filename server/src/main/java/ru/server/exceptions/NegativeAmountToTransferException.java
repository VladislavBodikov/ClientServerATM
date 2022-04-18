package ru.server.exceptions;

public class NegativeAmountToTransferException extends RuntimeException {

    public NegativeAmountToTransferException(String message) {
        super(message);
    }

    public NegativeAmountToTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
