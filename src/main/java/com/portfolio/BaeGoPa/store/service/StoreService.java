package com.portfolio.BaeGoPa.store.service;

import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreRepository;
import com.portfolio.BaeGoPa.store.db.StoreReviewEntity;
import com.portfolio.BaeGoPa.store.db.StoreReviewRepository;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    public List<StoreEntity> getAllStores() {
        return storeRepository.findAll();
    }

    public StoreEntity getStoreDetail(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
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
}
