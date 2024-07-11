package com.portfolio.BaeGoPa.menu.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.menu.db.MenuEntity;
import com.portfolio.BaeGoPa.menu.db.MenuReviewEntity;
import com.portfolio.BaeGoPa.menu.model.MenuRequest;
import com.portfolio.BaeGoPa.menu.model.MenuReviewRequest;
import com.portfolio.BaeGoPa.menu.service.MenuService;
import com.portfolio.BaeGoPa.store.db.StoreReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

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
}
