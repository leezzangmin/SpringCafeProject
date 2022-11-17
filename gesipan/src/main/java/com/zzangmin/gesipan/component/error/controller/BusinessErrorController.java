package com.zzangmin.gesipan.component.error.controller;

import com.zzangmin.gesipan.component.error.ErrorCode;
import com.zzangmin.gesipan.component.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class BusinessErrorController {
    /**
     *  IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_QUERY_ARGUMENT, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> illegalStateException(IllegalStateException e) {
        log.error("IllegalStateException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_QUERY_STATE, e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_INPUT_VALUE, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
