package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleIllegalArgumentException extends BabbleException {

    public BabbleIllegalArgumentException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
