package gg.babble.babble.controller;

import gg.babble.babble.exception.BabbleException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

    private final Logger logger = LoggerFactory.getLogger(BabbleAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> unexpectedException(final Exception exception) {
        logger.error(String.format(ERROR_LOG, exception.getClass().getSimpleName(), getStackTrace(exception)));
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected exception"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> unexpectedRuntimeException(final RuntimeException exception) {
        logger.error(String.format(ERROR_LOG, exception.getClass().getSimpleName(), getStackTrace(exception)));
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected runtime exception"));
    }

    private String getStackTrace(final Exception exception) {
        String trace = "";

        try (StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, true)) {
            exception.printStackTrace(printWriter);
            trace = stringWriter.getBuffer().toString();
        } catch (IOException e) {
            logger.error("스택 트레이스를 확인하는 도중 문제가 발생했습니다. {}", exception.getClass());
        }

        return trace;
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
