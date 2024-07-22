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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

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

    @GetMapping("/detail/{storeId}")
    public ExceptionApi<StoreEntity> getStoreDetail(@PathVariable Long storeId) {
        StoreEntity store = storeService.getStoreDetail(storeId);

        ExceptionApi<StoreEntity> response = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(store)
                .build();

        return response;
    }

    @GetMapping("/review/{storeId}")
    public ExceptionApi<List<StoreReviewEntity>> getStoreReviews(@PathVariable Long storeId) {
        List<StoreReviewEntity> reviews = storeService.getStoreReviews(storeId);

        ExceptionApi<List<StoreReviewEntity>> response = ExceptionApi.<List<StoreReviewEntity>>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(reviews)
                .build();

        return response;
    }

    @GetMapping("/review/{storeId}/{reviewId}")
    public ExceptionApi<StoreReviewEntity> getStoreReviewDetail(@PathVariable Long storeId, @PathVariable Long reviewId) {
        StoreReviewEntity review = storeService.getStoreReviewDetail(storeId, reviewId);
        ExceptionApi<StoreReviewEntity> response = ExceptionApi.<StoreReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(review)
                .build();
        return response;
    }

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

    @PutMapping("/edit/status/{storeId}")
    public ExceptionApi<StoreEntity> updateStoreStatus(
            @PathVariable Long storeId,
            @RequestBody StoreStatusRequest storeStatusRequest) {

        StoreEntity updatedStore = storeService.updateStoreStatus(storeId, storeStatusRequest.getStatus());

        ExceptionApi<StoreEntity> response = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(updatedStore)
                .build();

        return response;
    }

    @PutMapping("/edit/{storeId}")
    public ExceptionApi<StoreEntity> updateStore(
            @PathVariable Long storeId,
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

    @PutMapping("/review/edit/{storeId}/{reviewId}")
    public ExceptionApi<StoreReviewEntity> updateStoreReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
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

    @DeleteMapping("/review/delete/{storeReviewId}")
    public ExceptionApi<Void> deleteReview(@PathVariable Long storeReviewId){
        storeService.deleteReview(storeReviewId);

        ExceptionApi<Void> response = ExceptionApi.<Void>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .build();

        return response;
    }
}
