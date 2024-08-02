package com.portfolio.BaeGoPa.redis;

import com.portfolio.BaeGoPa.order.db.OrderEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Queue Management", description = "주문 큐 관리 API")
@RestController
@RequestMapping("/api/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @Operation(summary = "큐에 주문 추가", description = "주문을 큐에 추가")
    @PostMapping("/add")
    public String addToQueue(@RequestBody OrderEntity order) {
        queueService.addToQueue(order);
        return "Order added to queue";
    }

    @Operation(summary = "큐에서 주문 처리", description = "큐에서 주문을 가져와 처리")
    @GetMapping("/process")
    public String processQueue() {
        OrderEntity order = (OrderEntity) queueService.getFromQueue();
        if (order == null) {
            return "No orders to process";
        }
        // 주문 세부 정보를 포함한 메시지 출력
        return String.format("Processed order: ID=%d, StoreID=%d, ConsumerID=%d, TotalPrice=%.2f",
                order.getOrderId(),
                order.getStore().getStoreId(),
                order.getConsumer().getUserId(),
                order.getTotalPrice());
    }
}
