package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleIllegalStatementException extends BabbleException {

    public BabbleIllegalStatementException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
