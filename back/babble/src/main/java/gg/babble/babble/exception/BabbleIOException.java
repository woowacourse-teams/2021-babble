package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleIOException extends BabbleException {

    public BabbleIOException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
