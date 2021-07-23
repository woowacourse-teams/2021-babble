package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleDuplicatedException extends BabbleException {

    public BabbleDuplicatedException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }
}
