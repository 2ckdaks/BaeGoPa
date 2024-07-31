package com.portfolio.BaeGoPa.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    @Autowired
    private QueueService queueService;

    @PostMapping("/add")
    public String addToQueue(@RequestBody String order) {
        queueService.addToQueue(order);
        return "Order added to queue";
    }

    @GetMapping("/process")
    public String processQueue() {
        Object order = queueService.getFromQueue();
        if (order == null) {
            return "No orders to process";
        }
        // 여기서 주문을 처리하는 로직을 추가할 수 있습니다.
        return "Processed order: " + order.toString();
    }
}
