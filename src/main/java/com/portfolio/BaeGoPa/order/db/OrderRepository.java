package com.portfolio.BaeGoPa.order.db;

import com.portfolio.BaeGoPa.store.db.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStore(StoreEntity store);
}
