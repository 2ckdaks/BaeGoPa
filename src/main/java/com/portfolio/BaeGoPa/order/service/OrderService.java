package com.portfolio.BaeGoPa.order.service;

import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import com.portfolio.BaeGoPa.menu.db.MenuRepository;
import com.portfolio.BaeGoPa.order.db.OrderEntity;
import com.portfolio.BaeGoPa.order.db.OrderItemEntity;
import com.portfolio.BaeGoPa.order.db.OrderRepository;
import com.portfolio.BaeGoPa.order.model.OrderItemRequest;
import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreRepository;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;

    public OrderEntity createOrder(
            Long storeId,
            List<OrderItemRequest> orderItemsRequest
    ){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity consumer = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid consumer"));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store"));

        OrderEntity order = new OrderEntity();
        order.setConsumer(consumer);
        order.setStore(store);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderItemsRequest) {
            MenuEntity menu = menuRepository.findById(itemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu"));

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setMenu(menu);
            orderItem.setCount(itemRequest.getCount());
            orderItem.setPrice(menu.getPrice().multiply(BigDecimal.valueOf(itemRequest.getCount())));

            order.getOrderItems().add(orderItem);
            totalPrice = totalPrice.add(orderItem.getPrice());
        }

        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }
}
