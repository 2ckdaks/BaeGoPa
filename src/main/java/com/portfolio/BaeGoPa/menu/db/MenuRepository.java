package com.portfolio.BaeGoPa.menu.db;

import com.portfolio.BaeGoPa.store.db.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findByStoreId(StoreEntity store);
    void deleteByStoreId(StoreEntity store);
}
