package com.portfolio.BaeGoPa.menu.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuUpdateRequest {
    private String menuName;
    private String photoUrl;
    private BigDecimal price;
}
