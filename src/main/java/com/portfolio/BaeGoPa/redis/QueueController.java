package com.portfolio.BaeGoPa.redis;

import com.portfolio.BaeGoPa.order.db.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @PostMapping("/add")
    public String addToQueue(@RequestBody OrderEntity order) {
        queueService.addToQueue(order);
        return "Order added to queue";
    }

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
