package com.portfolio.BaeGoPa.order.db;

import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByMenu(MenuEntity menu);
}
