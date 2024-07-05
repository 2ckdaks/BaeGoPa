package com.portfolio.BaeGoPa.store.service;

import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreRepository;
import com.portfolio.BaeGoPa.user.db.UserEntity;
import com.portfolio.BaeGoPa.user.db.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

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
}
