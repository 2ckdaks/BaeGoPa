package com.portfolio.BaeGoPa.order.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.order.db.OrderEntity;
import com.portfolio.BaeGoPa.order.model.OrderRequest;
import com.portfolio.BaeGoPa.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ExceptionApi<OrderEntity> createOrder(
            @RequestBody OrderRequest orderRequest
    ){
      OrderEntity orderEntity = orderService.createOrder(
              orderRequest.getStoreId(),
              orderRequest.getOrderItems()
      );

        ExceptionApi<OrderEntity> createOrder = ExceptionApi.<OrderEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(orderEntity)
                .build();

        return createOrder;
    }
}
