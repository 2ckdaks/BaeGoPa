package com.portfolio.BaeGoPa.menu.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import com.portfolio.BaeGoPa.menu.db.MenuReviewEntity;
import com.portfolio.BaeGoPa.menu.model.MenuRequest;
import com.portfolio.BaeGoPa.menu.model.MenuReviewRequest;
import com.portfolio.BaeGoPa.menu.model.MenuReviewUpdateRequest;
import com.portfolio.BaeGoPa.menu.model.MenuUpdateRequest;
import com.portfolio.BaeGoPa.menu.service.MenuService;
import com.portfolio.BaeGoPa.store.db.StoreReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/{storeId}")
    public ExceptionApi<List<MenuEntity>> getMenus(@PathVariable Long storeId) {
        List<MenuEntity> menus = menuService.getMenus(storeId);

        ExceptionApi<List<MenuEntity>> response = ExceptionApi.<List<MenuEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(menus)
                .build();

        return response;
    }

    @GetMapping("/{storeId}/{menuId}")
    public ExceptionApi<MenuEntity> getMenuDetail(@PathVariable Long menuId){
        MenuEntity menu = menuService.getMenuDetail(menuId);

        ExceptionApi<MenuEntity> response = ExceptionApi.<MenuEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(menu)
                .build();

        return response;
    }

    @GetMapping("/review/{storeId}/{menuReviewId}")
    public ExceptionApi<MenuReviewEntity> getMenuReviewDetail(@PathVariable Long storeId, @PathVariable Long menuReviewId){
        MenuReviewEntity review = menuService.getMenuReviewDetail(storeId, menuReviewId);
        ExceptionApi<MenuReviewEntity> response = ExceptionApi.<MenuReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(review)
                .build();

        return response;
    }

    @PostMapping("/register")
    public ExceptionApi<MenuEntity> registerMenu(
            @RequestBody MenuRequest menuRequest
            ){
        MenuEntity menuEntity = menuService.registerMenu(
                menuRequest.getStoreId(),
                menuRequest.getMenuName(),
                menuRequest.getPhotoUrl(),
                menuRequest.getPrice()
        );

        ExceptionApi<MenuEntity> registerMenu = ExceptionApi.<MenuEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(menuEntity)
                .build();

        return registerMenu;
    }

    @PostMapping("/review/register")
    public ExceptionApi<MenuReviewEntity> registerReview(
            @RequestBody MenuReviewRequest menuReviewRequest
            ){
        MenuReviewEntity menuReviewEntity = menuService.registerReview(
                menuReviewRequest.getMenuId(),
                menuReviewRequest.getScore(),
                menuReviewRequest.getReview()
        );

        ExceptionApi<MenuReviewEntity> registerReview = ExceptionApi.<MenuReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(menuReviewEntity)
                .build();

        return registerReview;
    }

    @PutMapping("/edit/{menuId}")
    public ExceptionApi<MenuEntity> updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuUpdateRequest menuUpdateRequest
    ) {
        MenuEntity updatedMenu = menuService.updateMenu(
                menuId,
                menuUpdateRequest.getMenuName(),
                menuUpdateRequest.getPhotoUrl(),
                menuUpdateRequest.getPrice()
        );

        ExceptionApi<MenuEntity> response = ExceptionApi.<MenuEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedMenu)
                .build();

        return response;
    }

    @PutMapping("/review/edit/{menuReviewId}")
    public ExceptionApi<MenuReviewEntity> updateReview(
            @PathVariable Long menuReviewId,
            @RequestBody MenuReviewUpdateRequest menuReviewUpdateRequest) {

        MenuReviewEntity updatedReview = menuService.updateReview(
                menuReviewId,
                menuReviewUpdateRequest.getScore(),
                menuReviewUpdateRequest.getReview()
        );

        ExceptionApi<MenuReviewEntity> response = ExceptionApi.<MenuReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedReview)
                .build();

        return response;
    }

    @DeleteMapping("/delete/{menuId}")
    public ExceptionApi<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }

    @DeleteMapping("/review/delete/{menuReviewId}")
    public ExceptionApi<Void> deleteReview(@PathVariable Long menuReviewId){
        menuService.deleteReview(menuReviewId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }
}
