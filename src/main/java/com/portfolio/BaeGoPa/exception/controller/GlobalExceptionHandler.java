package com.portfolio.BaeGoPa.exception.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // RestApiExceptionHandler에서 예측하지 못한 모든 에러처리
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ExceptionApi> exception(
            Exception e
    ){
        log.error("", e);

        var response = ExceptionApi.builder()
                .resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .resultMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
