package com.portfolio.BaeGoPa.menu.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuRequest {
    private Long storeId;
    private String menuName;
    private BigDecimal price;
    private String photoUrl;
}
