package com.portfolio.BaeGoPa.order.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
