package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleLengthException extends BabbleException {

    public BabbleLengthException() {
        super();
    }

    public BabbleLengthException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }
}
