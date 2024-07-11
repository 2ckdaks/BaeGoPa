package com.portfolio.BaeGoPa.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long menuId;
    private int count;
}
