package com.portfolio.BaeGoPa.store.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreReviewRepository extends JpaRepository<StoreReviewEntity, Long> {
    List<StoreReviewEntity> findByStoreId(StoreEntity store);
    Optional<StoreReviewEntity> findByStoreIdAndReviewId(StoreEntity store, Long reviewId);
    void deleteByStoreId(StoreEntity store);
}
