package gg.babble.babble.controller;

import gg.babble.babble.exception.BabbleException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BabbleAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> unexpectedException() {
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected exception"));
    }

    @ExceptionHandler(BabbleException.class)
    public ResponseEntity<ExceptionDto> unexpectedRuntimeException() {
        return ResponseEntity.badRequest().body(new ExceptionDto("unexpected runtime exception"));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ExceptionDto {
        private String message;
    }
}
