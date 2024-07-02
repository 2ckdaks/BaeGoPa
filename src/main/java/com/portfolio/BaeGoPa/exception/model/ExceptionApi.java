package com.portfolio.BaeGoPa.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionApi<T> {

    private String resultCode; // http 상태코드

    private String resultMessage;

    private T data; // 항상 타입이 바뀔 수 있기에 generic으로 지정
}
