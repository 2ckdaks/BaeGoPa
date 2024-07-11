package com.portfolio.BaeGoPa.menu.db;

import com.portfolio.BaeGoPa.store.db.StoreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "menus")
@Getter
@Setter
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity storeId;

    @Column(nullable = false, length = 100)
    private String menuName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 255)
    private String photoUrl;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal score = BigDecimal.valueOf(0.0);

    @Column(nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;
}
