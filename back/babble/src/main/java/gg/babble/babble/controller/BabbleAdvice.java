package gg.babble.babble.controller;

import gg.babble.babble.exception.BabbleException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BabbleAdvice {

    private final String ERROR_LOG = "[ERROR] %s : %s";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> unexpectedException(Exception e) {
        logger.warn(String.format(ERROR_LOG, e.getClass().getSimpleName(), e.getMessage()));
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected exception"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> unexpectedRuntimeException(RuntimeException e) {
        logger.warn(String.format(ERROR_LOG, e.getClass().getSimpleName(), e.getMessage()));
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected runtime exception"));
    }

    @ExceptionHandler(BabbleException.class)
    public ResponseEntity<ExceptionDto> babbleException(final BabbleException babbleException) {
        logger.info(String.format(ERROR_LOG, babbleException.getClass().getSimpleName(), babbleException.getMessage()));
        return ResponseEntity.status(babbleException.status()).body(new ExceptionDto(babbleException.getMessage()));
    }

    @MessageExceptionHandler
    public ResponseEntity<ExceptionDto>  handleException(final BabbleException babbleException) {
        logger.info(String.format(ERROR_LOG, babbleException.getClass().getSimpleName(), babbleException.getMessage()));
        return ResponseEntity.status(babbleException.status()).body(new ExceptionDto(babbleException.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ExceptionDto {

        private String message;
    }
}
