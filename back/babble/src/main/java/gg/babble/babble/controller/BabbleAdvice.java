package gg.babble.babble.controller;

import gg.babble.babble.exception.BabbleException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BabbleAdvice {

    private final String ERROR_LOG = "[ERROR] %s : %s";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> unexpectedException(final Exception exception) {
        logger.error(String.format(ERROR_LOG, exception.getClass().getSimpleName(), exception.getMessage()));
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected exception"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> unexpectedRuntimeException(final RuntimeException exception) {
        logger.error(String.format(ERROR_LOG, exception.getClass().getSimpleName(), exception.getMessage()));
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected runtime exception"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDto>> methodArgumentValidException(final MethodArgumentNotValidException exception) {
        logger.info(String.format(ERROR_LOG, exception.getClass().getSimpleName(), exception.getMessage()));
        return ResponseEntity.badRequest().body(extractErrorMessages(exception));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ExceptionDto>> constraintViolationException(final ConstraintViolationException exception) {
        logger.info(String.format(ERROR_LOG, exception.getClass().getSimpleName(), exception.getMessage()));
        return ResponseEntity.badRequest().body(extractErrorMessages(exception));
    }

    private List<ExceptionDto> extractErrorMessages(final MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .map(ExceptionDto::new)
            .collect(Collectors.toList());
    }

    private List<ExceptionDto> extractErrorMessages(final ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .map(ExceptionDto::new)
            .collect(Collectors.toList());
    }

    @ExceptionHandler(BabbleException.class)
    public ResponseEntity<ExceptionDto> babbleException(final BabbleException exception) {
        logger.info(String.format(ERROR_LOG, exception.getClass().getSimpleName(), exception.getMessage()));
        return ResponseEntity.status(exception.status())
            .body(new ExceptionDto(exception.getMessage()));
    }

    @MessageExceptionHandler
    public ResponseEntity<ExceptionDto> handleException(final BabbleException exception) {
        logger.info(String.format(ERROR_LOG, exception.getClass().getSimpleName(), exception.getMessage()));
        return ResponseEntity.status(exception.status())
            .body(new ExceptionDto(exception.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ExceptionDto {

        private String message;
    }
}
