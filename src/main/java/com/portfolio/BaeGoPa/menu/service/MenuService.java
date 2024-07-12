package com.portfolio.BaeGoPa.menu.service;

import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import com.portfolio.BaeGoPa.menu.db.MenuRepository;
import com.portfolio.BaeGoPa.menu.db.MenuReviewEntity;
import com.portfolio.BaeGoPa.menu.db.MenuReviewRepository;
import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreRepository;
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
public class MenuService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuReviewRepository menuReviewRepository;

    public List<MenuEntity> getMenus(Long storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return menuRepository.findByStoreId(store);
    }

    public MenuEntity registerMenu(
            Long storeId,
            String menuName,
            String photoUrl,
            BigDecimal price
    ){
        // TODO : seller가 자신의 매장에만 메뉴 추가 가능하도록
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Username from SecurityContext: {}", username);
        UserEntity seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seller"));
        log.info("Seller from DB: {}", seller);
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store"));

        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setMenuName(menuName);
        menuEntity.setStoreId(store);
        menuEntity.setPrice(price);
        menuEntity.setPhotoUrl(photoUrl);

        return menuRepository.save(menuEntity);
    }

    public MenuReviewEntity registerReview(
            Long menuId,
            BigDecimal score,
            String review
    ) {
        // TODO : 리뷰작성자가 올바른지 검증 필요할듯?
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity consumer = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid consumer"));

        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu"));

        MenuReviewEntity menuReviewEntity = new MenuReviewEntity();
        menuReviewEntity.setMenu(menu);
        menuReviewEntity.setConsumer(consumer);
        menuReviewEntity.setScore(score);
        menuReviewEntity.setReview(review);

        return menuReviewRepository.save(menuReviewEntity);
    }
}
