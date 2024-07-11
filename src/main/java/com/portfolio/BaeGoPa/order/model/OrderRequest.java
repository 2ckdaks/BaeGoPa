package com.portfolio.BaeGoPa.order.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long storeId;
    private List<OrderItemRequest> orderItems;
}
