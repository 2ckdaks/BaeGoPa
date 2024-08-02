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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Menu API", description = "메뉴 관련 API")
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Operation(summary = "매장의 모든 메뉴 조회", description = "지정된 매장의 모든 메뉴를 반환")
    @GetMapping("/{storeId}")
    public ExceptionApi<List<MenuEntity>> getMenus(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId) {
        List<MenuEntity> menus = menuService.getMenus(storeId);

        ExceptionApi<List<MenuEntity>> response = ExceptionApi.<List<MenuEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(menus)
                .build();

        return response;
    }

    @Operation(summary = "메뉴 상세 조회", description = "지정된 메뉴 ID에 해당하는 메뉴의 상세 정보를 반환")
    @GetMapping("/{storeId}/{menuId}")
    public ExceptionApi<MenuEntity> getMenuDetail(
            @Parameter(description = "조회할 메뉴 ID") @PathVariable Long menuId){
        MenuEntity menu = menuService.getMenuDetail(menuId);

        ExceptionApi<MenuEntity> response = ExceptionApi.<MenuEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(menu)
                .build();

        return response;
    }

    @Operation(summary = "메뉴 리뷰 상세 조회", description = "지정된 매장과 메뉴 리뷰 ID에 해당하는 메뉴 리뷰의 상세 정보를 반환")
    @GetMapping("/review/{storeId}/{menuReviewId}")
    public ExceptionApi<MenuReviewEntity> getMenuReviewDetail(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId,
            @Parameter(description = "조회할 메뉴 리뷰 ID") @PathVariable Long menuReviewId){
        MenuReviewEntity review = menuService.getMenuReviewDetail(storeId, menuReviewId);
        ExceptionApi<MenuReviewEntity> response = ExceptionApi.<MenuReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(review)
                .build();

        return response;
    }

    @Operation(summary = "메뉴 등록", description = "새 메뉴를 등록")
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

    @Operation(summary = "메뉴 리뷰 등록", description = "새 메뉴 리뷰를 등록")
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

    @Operation(summary = "메뉴 정보 수정", description = "지정된 메뉴의 정보를 수정")
    @PutMapping("/edit/{menuId}")
    public ExceptionApi<MenuEntity> updateMenu(
            @Parameter(description = "수정할 메뉴 ID") @PathVariable Long menuId,
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

    @Operation(summary = "메뉴 리뷰 수정", description = "지정된 메뉴 리뷰의 정보를 수정")
    @PutMapping("/review/edit/{menuReviewId}")
    public ExceptionApi<MenuReviewEntity> updateReview(
            @Parameter(description = "수정할 메뉴 리뷰 ID") @PathVariable Long menuReviewId,
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

    @Operation(summary = "메뉴 삭제", description = "지정된 메뉴를 삭제")
    @DeleteMapping("/delete/{menuId}")
    public ExceptionApi<Void> deleteMenu(
            @Parameter(description = "삭제할 메뉴 ID") @PathVariable Long menuId) {
        menuService.deleteMenu(menuId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }

    @Operation(summary = "메뉴 리뷰 삭제", description = "지정된 메뉴 리뷰를 삭제")
    @DeleteMapping("/review/delete/{menuReviewId}")
    public ExceptionApi<Void> deleteReview(
            @Parameter(description = "삭제할 메뉴 리뷰 ID") @PathVariable Long menuReviewId){
        menuService.deleteReview(menuReviewId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }
}
