package com.portfolio.BaeGoPa.menu.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuReviewUpdateRequest {
    private BigDecimal score;
    private String review;
}
