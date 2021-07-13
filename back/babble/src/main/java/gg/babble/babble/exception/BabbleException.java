package gg.babble.babble.exception;

public abstract class BabbleException extends RuntimeException {

    public BabbleException() {
    }

    public BabbleException(String message) {
        super(message);
    }
}
