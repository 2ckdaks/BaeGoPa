package com.portfolio.BaeGoPa.menu.service;

import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import com.portfolio.BaeGoPa.menu.db.MenuRepository;
import com.portfolio.BaeGoPa.menu.db.MenuReviewEntity;
import com.portfolio.BaeGoPa.menu.db.MenuReviewRepository;
import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreRepository;
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

    public MenuEntity getMenuDetail(Long menuId){
        return menuRepository.findById(menuId)
                .orElseThrow(()-> new IllegalArgumentException("Invalid menu ID"));
    }

    public MenuReviewEntity getMenuReviewDetail(Long storeId, Long menuReviewId){
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
        return menuReviewRepository.findById(menuReviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID or review does not belong to the specified store"));
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

    @Transactional
    public MenuEntity updateMenu(Long menuId, String menuName, String photoUrl, BigDecimal price) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NoSuchElementException("Menu not found with id " + menuId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 권한 확인 (메뉴 소유자 또는 관리자만 수정 가능)
        if (!menu.getStoreId().getSeller().equals(user) && !user.getType().equals(UserType.ADMIN)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        menu.setMenuName(menuName);
        menu.setPhotoUrl(photoUrl);
        menu.setPrice(price);

        return menuRepository.save(menu);
    }

    @Transactional
    public MenuReviewEntity updateReview(Long menuReviewId, BigDecimal score, String review) {
        MenuReviewEntity menuReview = menuReviewRepository.findById(menuReviewId)
                .orElseThrow(() -> new NoSuchElementException("Menu review not found with id " + menuReviewId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // 권한 확인 (리뷰 작성자만 수정 가능)
        if (!menuReview.getConsumer().equals(user)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        menuReview.setScore(score);
        menuReview.setReview(review);

        return menuReviewRepository.save(menuReview);
    }
}
