package com.portfolio.BaeGoPa.store.service;

import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import com.portfolio.BaeGoPa.menu.db.MenuRepository;
import com.portfolio.BaeGoPa.order.db.OrderEntity;
import com.portfolio.BaeGoPa.order.db.OrderItemRepository;
import com.portfolio.BaeGoPa.order.db.OrderRepository;
import com.portfolio.BaeGoPa.store.db.*;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import com.portfolio.BaeGoPa.user.db.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @CacheEvict(value = "stores", allEntries = true)
    public StoreEntity registerStore(
            String storeName,
            String address,
            String phone,
            String category,
            String photoUrl
    ){
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
    @CacheEvict(value = "store", key = "#storeId")
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

    @Cacheable(value = "stores")
    public List<StoreEntity> getAllStores() {
        return storeRepository.findAll();
    }

    @Cacheable(value = "store", key = "#storeId")
    public StoreEntity getStoreDetail(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
    }

    @Cacheable(value = "storeReviews", key = "#storeId")
    public List<StoreReviewEntity> getStoreReviews(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return storeReviewRepository.findByStoreId(store);
    }

    @Cacheable(value = "storeReview", key = "{#storeId, #reviewId}")
    public StoreReviewEntity getStoreReviewDetail(Long storeId, Long reviewId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return storeReviewRepository.findByStoreIdAndReviewId(store, reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID or review does not belong to the specified store"));
    }

    @CacheEvict(value = "storeReviews", key = "#storeId")
    public StoreReviewEntity registerReview(
            Long storeId,
            BigDecimal score,
            String review
    ){
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
    @CacheEvict(value = "store", key = "#storeId")
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
    @CacheEvict(value = "storeReview", key = "{#storeId, #reviewId}")
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

    @Transactional
    @CacheEvict(value = "store", key = "#storeId")
    public void deleteStore(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException("Store not found with id " + storeId));

        // 로그인한 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 관리자 권한 검사
        if (!user.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("You do not have permission to delete this store");
        }

        // 관련된 리뷰 삭제
        storeReviewRepository.deleteByStoreId(store);

        // 관련된 메뉴와 주문 항목 삭제
        List<MenuEntity> menus = menuRepository.findByStoreId(store);
        for (MenuEntity menu : menus) {
            orderItemRepository.deleteByMenu(menu);
            menuRepository.delete(menu);
        }

        // 관련된 주문 삭제
        List<OrderEntity> orders = orderRepository.findByStore(store);
        for (OrderEntity order : orders) {
            orderItemRepository.deleteByOrder(order);
            orderRepository.delete(order);
        }

        // 매장 삭제
        storeRepository.delete(store);
    }

    @Transactional
    @CacheEvict(value = "storeReview", key = "#storeReviewId")
    public void deleteReview(Long storeReviewId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        StoreReviewEntity review = storeReviewRepository.findById(storeReviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found with id " + storeReviewId));

        // 리뷰 작성자 또는 관리자만 삭제 가능
        if (!review.getConsumer().equals(user) && !user.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        storeReviewRepository.delete(review);
    }
}
