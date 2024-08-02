package com.portfolio.BaeGoPa.order.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.order.db.OrderEntity;
import com.portfolio.BaeGoPa.order.model.OrderRequest;
import com.portfolio.BaeGoPa.order.model.OrderStatusUpdateRequest;
import com.portfolio.BaeGoPa.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order API", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "매장의 모든 주문 조회", description = "지정된 매장의 모든 주문을 반환")
    @GetMapping("/{storeId}")
    public ExceptionApi<List<OrderEntity>> getOrders(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId){
        List<OrderEntity> orders = orderService.getOrders(storeId);

        ExceptionApi<List<OrderEntity>> response = ExceptionApi.<List<OrderEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(orders)
                .build();

        return response;
    }

    @Operation(summary = "주문 상세 조회", description = "지정된 매장과 주문 ID에 해당하는 주문의 상세 정보를 반환")
    @GetMapping("/{storeId}/{orderId}")
    public ExceptionApi<OrderEntity> getOrderDetail(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId,
            @Parameter(description = "조회할 주문 ID") @PathVariable Long orderId) {
        OrderEntity order = orderService.getOrderDetail(storeId, orderId);

        ExceptionApi<OrderEntity> response = ExceptionApi.<OrderEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(order)
                .build();

        return response;
    }

    @Operation(summary = "주문 생성", description = "새 주문을 생성")
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

    @Operation(summary = "주문 상태 수정", description = "지정된 주문의 상태를 수정")
    @PutMapping("/edit/{orderId}")
    public ExceptionApi<OrderEntity> updateOrderStatus(
            @Parameter(description = "수정할 주문 ID") @PathVariable Long orderId,
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
