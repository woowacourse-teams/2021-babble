package gg.babble.babble.exception;

import org.springframework.http.HttpStatus;

public class BabbleAuthenticationException extends BabbleException {

    public BabbleAuthenticationException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
