package com.portfolio.BaeGoPa.store.db;

import com.portfolio.BaeGoPa.user.db.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_reviews")
@Getter
@Setter
public class StoreReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity storeId;

    @ManyToOne
    @JoinColumn(name = "consumer_id", nullable = false)
    private UserEntity consumer;

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal score;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String review;

    @Column(nullable = false, updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;
}
