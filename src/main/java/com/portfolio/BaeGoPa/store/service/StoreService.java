package com.portfolio.BaeGoPa.store.service;

import com.portfolio.BaeGoPa.store.db.*;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import com.portfolio.BaeGoPa.user.db.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @Autowired
    private UserRepository userRepository;


    public StoreEntity registerStore(
            String storeName,
            String address,
            String phone,
            String category,
            String photoUrl
    ){
        // TODO : 로그인 유저가 seller인지 확인 필요할듯?
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Username from SecurityContext: {}", username);
        UserEntity seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seller"));
        log.info("Seller from DB: {}", seller);

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setSeller(seller);
        storeEntity.setStoreName(storeName);
        storeEntity.setAddress(address);
        storeEntity.setPhone(phone);
        storeEntity.setCategory(category);
        storeEntity.setPhotoUrl(photoUrl);

        log.info("StoreEntity details - StoreName: {}, Address: {}, Phone: {}, Category: {}, PhotoUrl: {}",
                storeName, address, phone, category, photoUrl);

        return storeRepository.save(storeEntity);
    }

    @Transactional
    public StoreEntity updateStoreStatus(Long storeId, StoreStatus status) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException("Store not found with id " + storeId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 관리자 권한 확인
        if (admin.getType() != UserType.ADMIN) {
            throw new IllegalArgumentException("You do not have permission to update the store status");
        }

        store.setStatus(status);
        return storeRepository.save(store);
    }

    public List<StoreEntity> getAllStores() {
        return storeRepository.findAll();
    }

    public StoreEntity getStoreDetail(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
    }

    public List<StoreReviewEntity> getStoreReviews(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return storeReviewRepository.findByStoreId(store);
    }

    public StoreReviewEntity getStoreReviewDetail(Long storeId, Long reviewId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return storeReviewRepository.findByStoreIdAndReviewId(store, reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID or review does not belong to the specified store"));
    }

    public StoreReviewEntity registerReview(
            Long storeId,
            BigDecimal score,
            String review
    ){
        // TODO : 리뷰작성자가 올바른지 검증 필요할듯?
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity consumer = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid consumer"));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store"));

        StoreReviewEntity storeReviewEntity = new StoreReviewEntity();
        storeReviewEntity.setConsumer(consumer);
        storeReviewEntity.setStoreId(store);
        storeReviewEntity.setScore(score);
        storeReviewEntity.setReview(review);

        return storeReviewRepository.save(storeReviewEntity);
    }

    @Transactional
    public StoreEntity updateStore(Long storeId, String storeName, String address, String category, String phone, String photoUrl) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException("Store not found with id " + storeId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 매장의 seller이거나 ADMIN 유형인 계정만 수정 가능
        if (!store.getSeller().equals(user) && user.getType() != UserType.ADMIN) {
            throw new IllegalArgumentException("You do not have permission to update this store");
        }

        store.setStoreName(storeName);
        store.setAddress(address);
        store.setCategory(category);
        store.setPhone(phone);
        store.setPhotoUrl(photoUrl);

        return storeRepository.save(store);
    }

    @Transactional
    public StoreReviewEntity updateStoreReview(Long storeId, Long reviewId, BigDecimal score, String review) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        StoreReviewEntity storeReview = storeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found with id " + reviewId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 리뷰 작성자만 수정 가능
        if (!storeReview.getConsumer().equals(user)) {
            throw new IllegalArgumentException("You do not have permission to update this review");
        }

        storeReview.setScore(score);
        storeReview.setReview(review);

        return storeReviewRepository.save(storeReview);
    }
}
