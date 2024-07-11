package com.portfolio.BaeGoPa.store.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StoreReviewRequest {
    private Long storeId;
    private BigDecimal score;
    private String review;
}
