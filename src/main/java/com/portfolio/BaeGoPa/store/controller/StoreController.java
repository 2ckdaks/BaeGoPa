package com.portfolio.BaeGoPa.store.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreReviewEntity;
import com.portfolio.BaeGoPa.store.db.StoreStatus;
import com.portfolio.BaeGoPa.store.model.StoreRequest;
import com.portfolio.BaeGoPa.store.model.StoreReviewRequest;
import com.portfolio.BaeGoPa.store.model.StoreStatusRequest;
import com.portfolio.BaeGoPa.store.model.StoreUpdateRequest;
import com.portfolio.BaeGoPa.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Store API", description = "매장 관련 API")
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Operation(summary = "모든 매장 목록 조회", description = "시스템에 등록된 모든 매장의 목록을 반환")
    @GetMapping("/")
    public ExceptionApi<List<StoreEntity>> getAllStores() {
        List<StoreEntity> stores = storeService.getAllStores();

        ExceptionApi<List<StoreEntity>> response = ExceptionApi.<List<StoreEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(stores)
                .build();

        return response;
    }

    @Operation(summary = "매장 상세 조회", description = "지정된 매장의 상세 정보를 반환")
    @GetMapping("/detail/{storeId}")
    public ExceptionApi<StoreEntity> getStoreDetail(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId) {
        StoreEntity store = storeService.getStoreDetail(storeId);

        ExceptionApi<StoreEntity> response = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(store)
                .build();

        return response;
    }

    @Operation(summary = "매장 리뷰 목록 조회", description = "지정된 매장의 리뷰 목록을 반환")
    @GetMapping("/review/{storeId}")
    public ExceptionApi<List<StoreReviewEntity>> getStoreReviews(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId) {
        List<StoreReviewEntity> reviews = storeService.getStoreReviews(storeId);

        ExceptionApi<List<StoreReviewEntity>> response = ExceptionApi.<List<StoreReviewEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(reviews)
                .build();

        return response;
    }

    @Operation(summary = "매장 리뷰 상세 조회", description = "지정된 매장 리뷰의 상세 정보를 반환")
    @GetMapping("/review/{storeId}/{reviewId}")
    public ExceptionApi<StoreReviewEntity> getStoreReviewDetail(
            @Parameter(description = "조회할 매장 ID") @PathVariable Long storeId,
            @Parameter(description = "조회할 리뷰 ID") @PathVariable Long reviewId) {
        StoreReviewEntity review = storeService.getStoreReviewDetail(storeId, reviewId);
        ExceptionApi<StoreReviewEntity> response = ExceptionApi.<StoreReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(review)
                .build();
        return response;
    }

    @Operation(summary = "매장 등록", description = "새 매장을 등록")
    @PostMapping("/register")
    public ExceptionApi<StoreEntity> registerStore(
            @RequestBody StoreRequest storeRequest
    ){
        StoreEntity storeEntity = storeService.registerStore(
                storeRequest.getStoreName(),
                storeRequest.getAddress(),
                storeRequest.getCategory(),
                storeRequest.getPhone(),
                storeRequest.getPhotoUrl()
        );

        ExceptionApi<StoreEntity> registerStore = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(storeEntity)
                .build();

        return registerStore;
    }

    @Operation(summary = "매장 리뷰 등록", description = "새 매장 리뷰를 등록")
    @PostMapping("/review/register")
    public ExceptionApi<StoreReviewEntity> registerReview(
            @RequestBody StoreReviewRequest storeReviewRequest
            ){
        StoreReviewEntity storeReviewEntity = storeService.registerReview(
                storeReviewRequest.getStoreId(),
                storeReviewRequest.getScore(),
                storeReviewRequest.getReview()
        );

        ExceptionApi<StoreReviewEntity> registerReview = ExceptionApi.<StoreReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(storeReviewEntity)
                .build();

        return registerReview;
    }

    @Operation(summary = "매장 상태 수정", description = "지정된 매장의 상태를 수정")
    @PutMapping("/edit/status/{storeId}")
    public ExceptionApi<StoreEntity> updateStoreStatus(
            @Parameter(description = "수정할 매장 ID") @PathVariable Long storeId,
            @RequestBody StoreStatusRequest storeStatusRequest) {

        StoreEntity updatedStore = storeService.updateStoreStatus(storeId, storeStatusRequest.getStatus());

        ExceptionApi<StoreEntity> response = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedStore)
                .build();

        return response;
    }

    @Operation(summary = "매장 정보 수정", description = "지정된 매장의 정보를 수정")
    @PutMapping("/edit/{storeId}")
    public ExceptionApi<StoreEntity> updateStore(
            @Parameter(description = "수정할 매장 ID") @PathVariable Long storeId,
            @RequestBody StoreUpdateRequest storeUpdateRequest) {

        StoreEntity updatedStore = storeService.updateStore(
                storeId,
                storeUpdateRequest.getStoreName(),
                storeUpdateRequest.getAddress(),
                storeUpdateRequest.getCategory(),
                storeUpdateRequest.getPhone(),
                storeUpdateRequest.getPhotoUrl()
        );

        ExceptionApi<StoreEntity> response = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedStore)
                .build();

        return response;
    }

    @Operation(summary = "매장 리뷰 수정", description = "지정된 매장 리뷰의 정보를 수정")
    @PutMapping("/review/edit/{storeId}/{reviewId}")
    public ExceptionApi<StoreReviewEntity> updateStoreReview(
            @Parameter(description = "수정할 매장 ID") @PathVariable Long storeId,
            @Parameter(description = "수정할 리뷰 ID") @PathVariable Long reviewId,
            @RequestBody StoreReviewRequest storeReviewRequest) {

        StoreReviewEntity updatedReview = storeService.updateStoreReview(
                storeId,
                reviewId,
                storeReviewRequest.getScore(),
                storeReviewRequest.getReview()
        );

        ExceptionApi<StoreReviewEntity> response = ExceptionApi.<StoreReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedReview)
                .build();

        return response;
    }

    @Operation(summary = "매장 삭제", description = "지정된 매장을 삭제")
    @DeleteMapping("/delete/{storeId}")
    public ExceptionApi<Void> deleteStore(
            @Parameter(description = "삭제할 매장 ID") @PathVariable Long storeId) {
        storeService.deleteStore(storeId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }

    @Operation(summary = "매장 리뷰 삭제", description = "지정된 매장 리뷰를 삭제")
    @DeleteMapping("/review/delete/{storeReviewId}")
    public ExceptionApi<Void> deleteReview(
            @Parameter(description = "삭제할 매장 리뷰 ID") @PathVariable Long storeReviewId){
        storeService.deleteReview(storeReviewId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }
}
