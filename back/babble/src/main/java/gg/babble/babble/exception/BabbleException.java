package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public abstract class BabbleException extends RuntimeException {

    public BabbleException() {
    }

    public BabbleException(final String message) {
        super(message);
    }

    public abstract HttpStatus status();
}
