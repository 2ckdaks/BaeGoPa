package com.portfolio.BaeGoPa.order.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.order.db.OrderEntity;
import com.portfolio.BaeGoPa.order.model.OrderRequest;
import com.portfolio.BaeGoPa.order.model.OrderStatusUpdateRequest;
import com.portfolio.BaeGoPa.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{storeId}")
    public ExceptionApi<List<OrderEntity>> getOrders(@PathVariable Long storeId){
        List<OrderEntity> orders = orderService.getOrders(storeId);

        ExceptionApi<List<OrderEntity>> response = ExceptionApi.<List<OrderEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(orders)
                .build();

        return response;
    }

    @GetMapping("/{storeId}/{orderId}")
    public ExceptionApi<OrderEntity> getOrderDetail(@PathVariable Long storeId, @PathVariable Long orderId) {
        OrderEntity order = orderService.getOrderDetail(storeId, orderId);

        ExceptionApi<OrderEntity> response = ExceptionApi.<OrderEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(order)
                .build();

        return response;
    }

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

    @PutMapping("/edit/{orderId}")
    public ExceptionApi<OrderEntity> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest request
    ){
        OrderEntity orderEntity = orderService.updateOrderStatus(orderId, request.getStatus());

        ExceptionApi<OrderEntity> updateOrder = ExceptionApi.<OrderEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(orderEntity)
                .build();

        return updateOrder;
    }
}
