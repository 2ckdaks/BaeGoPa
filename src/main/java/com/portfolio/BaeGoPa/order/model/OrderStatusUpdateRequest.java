package com.portfolio.BaeGoPa.order.model;

import com.portfolio.BaeGoPa.order.db.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
