package com.portfolio.BaeGoPa.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String QUEUE_NAME = "orderQueue";

    public void addToQueue(Object order) {
        redisTemplate.opsForList().leftPush(QUEUE_NAME, order);
    }

    public Object getFromQueue() {
        return redisTemplate.opsForList().rightPop(QUEUE_NAME);
    }
}
