package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleNotFoundException extends BabbleException {

    public BabbleNotFoundException() {
        super();
    }

    public BabbleNotFoundException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}

