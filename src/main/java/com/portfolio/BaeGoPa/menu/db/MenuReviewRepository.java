package com.portfolio.BaeGoPa.menu.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuReviewRepository extends JpaRepository<MenuReviewEntity, Long> {
//    Optional<MenuReviewEntity> findByMenu_StoreIdAndReviewId(Long storeId, Long reviewId);
}
