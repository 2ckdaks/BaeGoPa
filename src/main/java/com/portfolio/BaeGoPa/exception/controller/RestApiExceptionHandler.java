package com.portfolio.BaeGoPa.exception.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
@Order(1) // 기본값이 최소값이므로 GlobalExceptionHandler에는 주지않아도 됨 먼저 해당 handler를 지나기 위함)
public class RestApiExceptionHandler {

    // NotFound 에러처리
    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<ExceptionApi> noSuchElement(
            NoSuchElementException e
    ){
        log.error("NoSuchElementException", e);

        var response = ExceptionApi.builder()
                .resultCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .resultMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // BadRequest 에러 처리
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionApi> handleBadRequestException(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);

        var response = ExceptionApi.builder()
                .resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .resultMessage("잘못된 요청입니다.") // 한국어 메시지로 변경
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
