package com.portfolio.BaeGoPa.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.BaeGoPa.order.db.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String QUEUE_NAME = "orderQueue";

    @Autowired
    private ObjectMapper objectMapper;

    public void addToQueue(Object order) {
        redisTemplate.opsForList().leftPush(QUEUE_NAME, order);
    }

    public OrderEntity getFromQueue() {
        Object order = redisTemplate.opsForList().rightPop(QUEUE_NAME);
        return objectMapper.convertValue(order, OrderEntity.class);
    }
}
