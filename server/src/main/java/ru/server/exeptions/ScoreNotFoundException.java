package ru.server.exeptions;

public class ScoreNotFoundException extends RuntimeException {

    public ScoreNotFoundException(String message) {
        super(message);
    }
}
